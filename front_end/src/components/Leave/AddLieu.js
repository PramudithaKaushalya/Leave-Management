
import React from 'react';
import 'antd/dist/antd.css';
import axios from '../../config/axios'
import moment from 'moment';
import {
    Form,
    Input,
    Button,
    DatePicker,
    Card,
    Icon,
    message,
    Modal,
    Radio,
    Spin,
    Select,
    Row,
    Col,
    Table
} from 'antd';

const { TextArea } = Input;
const { confirm } = Modal; 
const { Option } = Select;

class AddLieu extends React.Component {

    state = {
        holidays : [],
        spinning : false,
        employees : [],
        saveDates : [],
    };

    componentWillMount() {
        this.getUsers();
        this.getHolidayEvents();
        this.getHolidays();
    }

    getHolidays() {
        axios.get('calender/dates', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {  
                this.setState({
                holidays : res.data.list
                });
            } else {
                message.error(res.data.message);
            }
        })
        .catch(e => {
            message.error("Something went wrong");
            console.log(e.response.data.error);
        })
    }

    getHolidayEvents() {
        axios.get('calender/all', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {  
            this.setState({
                saveDates : res.data.list
            });
            } else {
            message.error(res.data.message);
            }
        })
        .catch(e => {
            message.error("Something went wrong");
            console.log(e.response.data.error);
        })
    }

    getUsers () {

        axios.get('user/manage_emp_filter', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {  
                this.setState({
                    employees : res.data.list
                })
            } else {
                message.error(res.data.message);
            }
        })
        .catch(e => {
            message.error("Something went wrong");
            console.log(e.response.data.error);
        })
}

    handleSubmit = e => {
        e.preventDefault();

        this.props.form.validateFieldsAndScroll((err, values) => {
        
            if (!err) {
                confirm({
                    title: 'Sure to add this lieu request?',
                    content: 'If you submit, this request will be sent to supervisors to get approval.',
                    okText: 'Submit',
                    okType: 'primary',
                    onOk: () => {
                        if(!this.state.holidays.includes(moment(values.date).format("YYYY-MM-DD")) && values.date._d.getDay() !== 0 && values.date._d.getDay() !== 6){
                        message.error("Not a holiday or weekend"); 
                        } else {
                        this.saveRequest(values);  
                        }
                    }
                })
            }
        })
    }

    saveRequest = (values) => {

        this.setState({
            spinning : true
        });

        const leave = {
            id : values.employee,
            date : values.date,
            period : values.period,
            project : values.project,
            worksDone : values.worksDone
        }

        axios.post(
            'lieu_leave/save_by_admin', 
            leave, 
            { 
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem("header")
                }
            }
        )
        .then(res => {
            if (res.data.success) {  
                this.handleCancel();            
                message.success(res.data.message); 

                this.setState({
                    spinning : false
                });
            } else {
                message.error(res.data.message);
                this.setState({
                spinning : false
                });
            }
        })
        .catch(e => {
            console.log(e.response.data.error);
            message.error("Something went wrong"); 

            this.setState({
            spinning : false
            });
        })
    };

    handleCancel = () => {
        this.props.form.resetFields();
    };


render() {
    const { getFieldDecorator } = this.props.form;
    const { saveDates, spinning } = this.state;

    const formItemLayout = {
        labelCol: {
            xs: { span: 24 },
            sm: { span: 8 },
        },
        wrapperCol: {
            xs: { span: 24 },
            sm: { span: 16 },
        },
    };
    
    const tailFormItemLayout = {
        wrapperCol: {
            xs: {
            span: 24,
            offset: 0,
            },
            sm: {
            span: 16,
            offset: 8,
            },
        },
    };

    const columnsForHolidays = [
        {
            title: 'Date',
            key: '0',
            dataIndex: 'date'
        },
        {
            title: 'Event',
            key: '1',
            dataIndex: 'reason',
        },    
    ];

    return (
        <div>
        <Spin tip="Sending..." spinning={spinning}>
          <Row gutter={16}>
            <Col span={13} > 
            <Card type="inner" title='Add Lieu Leaves to Employees' bordered={false} hoverable='true'>  
                <Form {...formItemLayout} onSubmit={this.handleSubmit}>

                    {this.state.employees.length!==0?  
                    <Form.Item label="Select employee">
                    {getFieldDecorator('employee', {
                        rules: [{ required: true, message: 'Please select the employee!' }],
                        initialValue : 'No One'
                    })(
                        <Select placeholder="Please select a employee" >
                        {this.state.employees.map(item => (
                            <Option key={item.id}>{item.firstName} {item.secondName}</Option>
                        ))}
                    </Select> )}
                    </Form.Item>
                    : null }    
                    <Form.Item label="Date">
                    {getFieldDecorator('date', {
                        rules: [{ required: true, message: 'Please input Date!' }],
                    })(
                    <DatePicker
                        format="YYYY-MM-DD"
                        placeholder="Pickup a Date"
                        style={{ width: '100%' }}
                    />
                    )}
                    </Form.Item>

                    <Form.Item label="Period">
                    {getFieldDecorator('period', {
                        rules: [{ required: true, message: 'Please input worked period!' }],
                        initialValue: 0
                    })(
                        <Radio.Group >
                            <Radio.Button value={0}>Half Day</Radio.Button>
                            <Radio.Button value={1}>Full Day</Radio.Button>
                        </Radio.Group>
                    )}
                    </Form.Item>                

                    <Form.Item label="Project">
                    {getFieldDecorator('project', {
                        rules : [{ 
                        required: true, 
                        message: 'Please input the project!' 
                        }],
                    })(<Input maxLength = {30}/>)}
                    </Form.Item>
                
                    <Form.Item label="Comment">
                    {getFieldDecorator('worksDone', {
                        rules: [{
                            required: true, 
                            message: 'Please input the works you done',
                        }],
                    })(<TextArea rows={4} maxLength = {300} style={{ width: '100%', height: '170px' }}/>)}
                    </Form.Item>
                    <br/>
                    <Form.Item {...tailFormItemLayout}>
                    <Button type="primary" htmlType="submit" style={{width:'100px'}}>
                    <Icon type="check-circle" /> 
                        Submit
                    </Button>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <Button type="danger" onClick={this.handleCancel} style={{width:'100px'}}>
                    <Icon type="close-circle" /> 
                        Cancel
                    </Button>
                    </Form.Item>
                </Form>
                <br/>
            </Card>
            </Col>
            <Col span={11}>
                <Card hoverable='true'>
                    <h4>Holidays</h4>
                    <Table rowKey={record => record.date} columns={columnsForHolidays} dataSource={saveDates} size="small" pagination={{pageSize: 12}}/>
                </Card>
            </Col>
            </Row>
        </Spin>
    </div>
    );
    }
}

const WrappedAddLieu = Form.create({ name: 'register' })(AddLieu);

export default WrappedAddLieu;         