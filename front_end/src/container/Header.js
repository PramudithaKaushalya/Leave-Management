
import React from 'react';
import 'antd/dist/antd.css';
import {withRouter} from 'react-router-dom';
import moment from 'moment';
import axios from '../config/axios';
import { Button, Modal, Row, Col, Avatar, Tag, Drawer, List, message, Alert, Card, Badge } from 'antd';

const { confirm } = Modal;


class Header extends React.Component { 

    componentWillMount () {
        this.getPendingLeaveCount();
    }

    handleLogout = (e) => {
        e.preventDefault();
        confirm({
            title: 'Do you want to logout?',
            onOk: () => {

                localStorage.removeItem("header");
                this.props.history.push('/login');
                // axios.get('api/auth/logout', 
                // {
                //     headers: {
                //         Authorization: 'Bearer ' + localStorage.getItem("header")
                //     }
                // })
                // .then(res => {
                //     if (res.data.success === true) {
                //         localStorage.removeItem("header");
                //         this.props.history.push('/login');
                //     } else {
                //         message.error(res.data.message);
                //     }
                // }).catch( err => {
                //     console.log(err.response.data.error);
                //     message.error("Something went wrong");
                // })
            },
            onCancel() {
                console.log('Cancel');
            },
        });
    }

    state = {
        absentRes: false,
        requestedRes: false,
        rejectedRes: false,
        visible: false,
        data: [],
        requested: [],
        rejected: [],
        pendingCount: '0'
    };

    toggle = () => {
        this.setState({
            absentRes: false,
            requestedRes: false,
            rejectedRes: false,
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
                    data : res.data.list,
                    absentRes: true
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
                    requested : res.data.list,
                    requestedRes: true
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
                    rejected : res.data.list,
                    rejectedRes: true
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

    getPendingLeaveCount = () => {
        axios.get('leave/pending_count', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {
                this.setState({
                    pendingCount : res.data.message,
                })
            } else {
                message.error(res.data.message);
            }
        }).catch( err => {
            console.log("Error",err);
            message.error("Something went wrong");
        })
    }

    directToPending = () => {
        this.props.history.push('/pending_leaves');
    }

    render () {

    setInterval(this.getPendingLeaveCount, 3600000);
    return (
        <div>
        {/* <PageHeader
            style={{   
            border: '2px solid rgb(235, 237, 240)'
            }}
        > */}
        <Card hoverable='true'> 
            <Row>
                <Col span={3}>
                    {
                        this.props.image === null ?
                        <Avatar size={90} icon="user" src="/public/images/01.png"/>
                        :
                        <Avatar size={90} icon="user" src= {this.props.image}/>
                    }
                </Col>
                <Col span={16}>
                    <br/>
                    <span style={{fontSize:'17px'}}>{this.props.name}</span><br/>
                    <span style={{fontSize:'13px'}}>{this.props.designation}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style={{fontSize:'13px', color:'gray'}}>{this.props.department}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style={{fontSize:'13px', color:'red'}}>{this.props.role}</span> 
                </Col>
                <Col span={5}>
                    <Row>
                        <Col span={5}>
                            <Button key="1" type="primary" onClick={this.toggle} icon= 'menu-fold'/>
                        </Col>
                        { this.props.role === 'Admin' || this.props.role === 'Supervisor' ?
                        <Col span={5}>
                            <Badge count={parseInt(this.state.pendingCount)} offset={[0, 0]}>
                                <Button key="1" type="primary" onClick={this.directToPending} icon= 'bell'/>
                            </Badge>
                        </Col> 
                        : null}
                        <Col span={1}>
                            <Button key="2" type="primary" onClick={this.handleLogout} icon="logout"/>
                        </Col>
                    </Row>
                    <br/>
                    <Row>
                        <Tag color="volcano">{moment().format('LL')}</Tag>
                    </Row>
                </Col>
            </Row>
        </Card>
        {/* </PageHeader> */}

            <Drawer    
            title="Today Leaves"                                                                                                                                                                                                                                                                                                                                     
            width={300}
            onClose={this.onClose}
            visible={this.state.visible}
            >
                <br/>
                <Alert message="Approved" type="success" showIcon />
                { this.state.absentRes ? 
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.data}
                    renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                        avatar={<Avatar src={item.image}  icon="user"/>}
                        title={item.name}
                        />
                    </List.Item>
                    )}
                />
                : null }
                <br/><br/>
                <Alert message="Pending" type="info" showIcon /> 
                { this.state.rejectedRes ?        
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.requested}
                    renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                        avatar={<Avatar src={item.image}  icon="user"/>}
                        title={item.name}
                        />
                    </List.Item>
                    )}
                />
                : null }

                <br/><br/>
                { this.props.role === 'Admin' || this.props.role === "Supervisor"? 
                <Alert message="Rejected" type="error" showIcon />
                : null }
                { this.state.rejectedRes && (this.props.role === 'Admin' || this.props.role === "Supervisor")? 
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.rejected}
                    renderItem={item => (
                    <List.Item>
                        <List.Item.Meta
                        avatar={<Avatar src={item.image}  icon="user"/>}
                        title={item.name}
                        />
                    </List.Item>
                    )}
                />
                : null }
                
            </Drawer>

        </div>
        );
    }
}          

export default withRouter(Header);

// shape="circle"