import React, { Component } from 'react';
import axios from 'axios';
import 'antd/dist/antd.css';
import { Card, Table, Modal, Tag, Row, Col, Icon, Select, message } from 'antd';

const { Option } = Select;

function info(leave) {
    console.log(this.state.count.casual);
    Modal.info({
      title: 'Fully Leave Record',
      content: (
        <div>
         
        </div>
      ),
      onOk() {},
    });
}

function handleChange(value) {
    console.log(`selected ${value}`);
  }
  
class OneAttendence extends Component {
    
    componentWillMount(){
        this.setState({
            data : [],
            count : []
        })
        axios.get('http://localhost:5000/leave/find/'+localStorage.getItem("leave_count"), 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {
                this.setState({
                    data : (res.data.list).reverse()
                })
            } else {
                  message.error(res.data.message);
            }
        }) 
        .catch(e => {
            message.error("Something went wrong");
            console.log(e.response.data.error);
        })

        axios.get('http://localhost:5000/leave_count/profile/'+localStorage.getItem("leave_count"), 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {            
            if (res.data.success === true) {
                this.setState({
                    count : res.data.profile,
                    special : res.data.profile.casual
                })
            } else {
                  message.error(res.data.message);
            }
        }) 
        .catch(e => {
            message.error("Something went wrong");
            console.log(e.response.data.error);
        }) 

        const employee = { emp_id: localStorage.getItem("leave_count") } 
        axios.post('http://localhost:5000/contact/employee', employee,
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            console.log("contact", res.data);
            
            if (res.data.success === true) {
                this.setState({
                    count : res.data.profile,
                    special : res.data.profile.casual
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

    state = {
        data : [],
        count : [],
        contact : [],
        special : null
    }

    render() {

        const columns = [ 
            
            {
                title: 'Date of request',
                dataIndex: 'requestDateTime',
            },
            {
                title: 'Leave Type',
                dataIndex: 'type'
            },
            {
                title: 'Start Date',
                dataIndex: 'startDate'
            },
            {
                title: 'End Date',
                dataIndex: 'endDate'
            },
            {
                title: 'Number of days',
                dataIndex: 'number_of_leave_days'
            },
            {
                title: 'Status',
                dataIndex: 'status',
            },
            {
              dataIndex: '',
              key: 'z',
              width: '1%',
              render: (emp) => <Icon type="info-circle" onClick={info.bind(this, emp)}  theme="twoTone" />,
            }
        ];

        return (
            <div className='center'>
                <Card hoverable='true'>
                <Row>
                <Col span={1}><Tag color="volcano">0004</Tag></Col>
                    <Col span={18}><Tag color="geekblue">Pramuditha Kaushalya</Tag></Col>
                    <Col span={5}>
                        <Select defaultValue="jan" style={{ width: 200 }} onChange={handleChange}>
                        <Option value="jan">January</Option>
                        <Option value="feb">February</Option>
                        <Option value="mar">March</Option>
                        <Option value="apr">April</Option>
                        <Option value="may">May</Option>
                        <Option value="jun">June</Option>
                        <Option value="jul">July</Option>
                        <Option value="aug">Augest</Option>
                        <Option value="sep">September</Option>
                        <Option value="oct">Octomber</Option>
                        <Option value="nov">November</Option>
                        <Option value="dec">December</Option>
                        </Select>
                    </Col>
                </Row>

                </Card>
                <br/>

                <Card hoverable='true' >
                    <Table rowKey={record => record.id} columns={columns} dataSource={this.state.data}  pagination={{ pageSize: 7 }} size="middle"/>
                </Card>
              
            </div>
        )
    }
}    

export default OneAttendence;
