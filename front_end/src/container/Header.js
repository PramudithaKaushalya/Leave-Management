
import React from 'react';
import 'antd/dist/antd.css';
import {withRouter} from 'react-router-dom';
import moment from 'moment';
import axios from '../config/axios';
import { PageHeader, Icon, Button, Modal, Row, Col, Avatar, Tag, Drawer, List, message, Alert } from 'antd';

const { confirm } = Modal;

class Header extends React.Component { 

    handleLogout = (e) => {
        e.preventDefault();
        confirm({
          title: 'Do you want to logout?',
          onOk: () => {

            axios.get('api/auth/logout', 
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem("header")
                }
            })
            .then(res => {
                if (res.data.success === true) {
                    localStorage.removeItem("header");
                    this.props.history.push('/login');
                } else {
                    message.error(res.data.message);
                }
            }).catch( err => {
                console.log(err.response.data.error);
                message.error("Something went wrong");
            })
          },
          onCancel() {
            console.log('Cancel');
          },
        });
    }

    componentDidMount () {
    }
    state = {
        visible: false,
        data: [],
        requested: [],
        rejected: []
    };

    toggle = () => {
        this.setState({
            data : [],
            visible: true
        })
        
        axios.get('leave/absent', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {

                this.setState({
                    data : res.data.list
                })
            } else {
                message.error(res.data.message);
            }
        }).catch( err => {
            console.log("Error",err);
            message.error("Something went wrong");
        })


        axios.get('leave/requested', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {

                this.setState({
                    requested : res.data.list
                })
            } else {
                message.error(res.data.message);
            }
        }).catch( err => {
            console.log("Error",err);
            message.error("Something went wrong");
        })

        axios.get('leave/rejected', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {
                this.setState({
                    rejected : res.data.list
                })
            } else {
                message.error(res.data.message);
            }
        }).catch( err => {
            console.log("Error",err);
            message.error("Something went wrong");
        })
    };

    onClose = () => {
        this.setState({
          visible: false
        });
    };

    render () {

    return (
        <div>
        <PageHeader
            style={{   
            border: '1px solid rgb(235, 237, 240)',
            backgroundImage: 'https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSQhsbd4x-xRzJrNDxCCL0qT7mQa_tk4jS9WvFALCBAbKFSWwqw',
            }}
        >
            <Row>
                <Col span={1}>
                    <Avatar size={50} icon="user"  src= {this.props.image}/>
                </Col>
                <Col span={18}>
                    <span style={{fontSize:'17px'}}>{this.props.name}</span><br/>
                    <span style={{fontSize:'13px'}}>{this.props.designation}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style={{fontSize:'13px', color:'gray'}}>{this.props.department}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style={{fontSize:'13px', color:'red'}}>{this.props.role}</span> 
                </Col>
                <Col span={4}>
                    <Tag color="volcano">{moment().format('LL')}</Tag>&nbsp;&nbsp;&nbsp;&nbsp;
                    <Button key="1" type="primary" onClick={this.toggle} shape="circle">
                        <Icon 
                            className="trigger"
                            type= 'menu-fold' 
                        />
                    </Button>&nbsp;&nbsp;&nbsp;
                    <Button key="2" type="primary" onClick={this.handleLogout} shape="circle">
                        <Icon type="logout" />
                    </Button>
                </Col>
            </Row>
        </PageHeader>

        { this.state.data.length !== 0 && this.state.requested.length !== 0 && this.state.rejected.length !== 0 ? 
            <Drawer    
            title="Today Attendence"                                                                                                                                                                                                                                                                                                                                     
            width={300}
            onClose={this.onClose}
            visible={this.state.visible}
            >
                <br/>
                <Alert message="Approved" type="success" showIcon />
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.data}
                    renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                        avatar={<Avatar src={item.image} />}
                        title={item.name}
                        />
                    </List.Item>
                    )}
                />
                
                <br/><br/>
                <Alert message="Requested" type="info" showIcon />        
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.requested}
                    renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                        avatar={<Avatar src={item.image} />}
                        title={item.name}
                        />
                    </List.Item>
                    )}
                />
                <br/><br/>
                <Alert message="Rejected" type="error" showIcon />
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.rejected}
                    renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                        avatar={<Avatar src={item.image} />}
                        title={item.name}
                        />
                    </List.Item>
                    )}
                />
                
            </Drawer>
            : null
            }    
        </div>
        );
    }
}          

export default withRouter(Header);