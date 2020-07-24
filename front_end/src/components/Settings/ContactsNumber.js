import React from "react";
import { Icon, Form, Button, Input, Card, Table, Modal, message, Spin } from 'antd';
import Highlighter from 'react-highlight-words';
import axios from '../../config/axios';
import './index.css';

const { confirm } = Modal;

class ContactsNumber extends React.Component {

    componentWillMount() {
        this.reload();
    }

    reload() {
        if (localStorage.getItem("header") !== null) {
            const employee = { id: this.parseJwt(localStorage.getItem("header")) }
            axios.post('contact/employee', employee,
                {
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem("header")
                    }
                })
                .then(res => {
                    if (res.data.success === true) {
                        this.setState({
                            data: res.data.list,
                            res : true
                        })
                    } else {
                        message.error(res.data.message);
                    }
                }).catch(e => {
                    console.log(e);
                })
        }
    }

    parseJwt(token) {
        var base64Url = token.split('.')[1];
        var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        var jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload).sub;
    };

    state = {
        data: [],
        res : false
    }

    handleContact = e => {
        e.preventDefault();

        this.props.form.validateFieldsAndScroll((err, values) => {
            if (!err) {
                console.log(values);

                const contact = {
                    name: values.name || undefined,
                    contact: values.contact || undefined,
                    relation: values.relation || undefined,
                    user: { id: this.parseJwt(localStorage.getItem("header")) } || undefined
                }

                axios.post('contact/add', contact,
                    {
                        headers: {
                            Authorization: 'Bearer ' + localStorage.getItem("header")
                        }
                    })
                    .then(res => {
                        if (res.data.success === true) {
                            message.success(res.data.message);
                            this.handelCancel();
                            this.reload();
                        } else {
                            message.error(res.data.message);
                        }
                    })
                    .catch(e => {
                        message.error("Unsuccessfull");
                        console.log("res", e)
                    })
            }
        })

    }

    getColumnSearchProps = dataIndex => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={node => {
                        this.searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => this.handleSearch(selectedKeys, confirm)}
                    style={{ width: 188, marginBottom: 8, display: 'block' }}
                />
                <Button
                    type="primary"
                    onClick={() => this.handleSearch(selectedKeys, confirm)}
                    icon="search"
                    size="small"
                    style={{ width: 90, marginRight: 8 }}
                >
                    Search
            </Button>
                <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{ width: 90 }}>
                    Reset
            </Button>
            </div>
        ),
        filterIcon: filtered => (
            <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
        ),
        onFilter: (value, record) =>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => this.searchInput.select());
            }
        },
        render: text => (
            <Highlighter
                highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
                searchWords={[this.state.searchText]}
                autoEscape
                textToHighlight={text.toString()}
            />
        ),
    });

    handleDelete(contact, e) {
        e.preventDefault();
        confirm({
            title: 'Do you want to remove this contact ?',
            okText: 'Remove',
            okType: 'danger',
            onOk: () => {
                axios.get('contact/delete/' + contact.contact_id,
                    {
                        headers: {
                            Authorization: 'Bearer ' + localStorage.getItem("header")
                        }
                    })
                    .then(res => {
                        if (res.data.success === true) {
                            message.success(res.data.message);
                            this.handelCancel();
                            this.reload();
                        } else {
                            message.error(res.data.message);
                        }
                    }).catch(err => {
                        console.log(err)
                        message.error("Unsuccessfull");
                    })
            }
        })
    }

    handelCancel = () => {
        this.props.form.resetFields();
    }

    render() {

        const { getFieldDecorator } = this.props.form;

        const columns = [
            {
                title: 'Name',
                dataIndex: 'name'
            },
            {
                title: 'Contact',
                dataIndex: 'contact'
            },
            {
                title: 'Relation',
                dataIndex: 'relation',
            },
            {
                dataIndex: '',
                key: 'x',
                render: (emp) => <Icon type="delete" onClick={this.handleDelete.bind(this, emp)} theme="twoTone" twoToneColor='#EE204D' />,
            },
        ];

        return (
            <div>
                <div>
                    <Card type="inner" title='Add Emergency Contacts' bordered={false} hoverable='true'>
                        <Form layout="inline" hideRequiredMark>

                            <Form.Item >
                                {getFieldDecorator("name", {
                                    rules: [
                                        {
                                            required: true,
                                            message: "Please input person !"
                                        }
                                    ]
                                })(<Input
                                    id='1'
                                    prefix={<Icon type="user" style={{ color: "rgba(0,0,0,.25)" }} />}
                                    placeholder="&nbsp;&nbsp;&nbsp;&nbsp;Name"
                                    maxLength='50'
                                />)}
                            </Form.Item>

                            <Form.Item >
                                {getFieldDecorator("contact", {
                                    rules: [
                                        {
                                            required: true,
                                            message: 'Please input contact number!'
                                        },
                                        {
                                            message: 'Please input valid number!',
                                            len: 10
                                        }
                                    ]
                                })(<Input
                                    id='2'
                                    prefix={<Icon type="phone" style={{ color: "rgba(0,0,0,.25)" }} />}
                                    placeholder="&nbsp;&nbsp;&nbsp;&nbsp;Contact Number"
                                />)}
                            </Form.Item>

                            <Form.Item >
                                {getFieldDecorator("relation", {
                                    rules: [
                                        {
                                            required: true,
                                            message: "Please input relation !"
                                        }
                                    ]
                                })(<Input
                                    id='3'
                                    onBlur={this.handleConfirmBlur}
                                    prefix={<Icon type="home" style={{ color: "rgba(0,0,0,.25)" }} />}
                                    placeholder="&nbsp;&nbsp;&nbsp;&nbsp;Relation"
                                    maxLength='20'
                                />)}
                            </Form.Item>

                            &nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;
                    <Form.Item>
                                <Button type="primary" onClick={this.handleContact} style={{ width: '100px' }}>
                                    <Icon type="check-circle" />
                                    Submit
                        </Button>
                                &nbsp;&nbsp;&nbsp;
                        <Button type="danger" onClick={this.handelCancel} style={{ width: '100px' }}>
                                    <Icon type="close-circle" />
                                    Cancel
                        </Button>
                            </Form.Item>
                        </Form>
                    </Card>
                </div>
                <br />
                <div>
                { this.state.res? 
                    <Card hoverable='true'>
                        <Table rowKey={record => record.contact_id} columns={columns} dataSource={this.state.data} pagination={{ pageSize: 5 }} size="middle" />
                    </Card>
                 : 
                 <div className="example2">
                   <Spin size="large" />
                 </div>
                 } 
                </div>
            </div>
        );
    }
}

const WrappedContactNumber = Form.create({ name: 'register' })(ContactsNumber);

export default WrappedContactNumber;