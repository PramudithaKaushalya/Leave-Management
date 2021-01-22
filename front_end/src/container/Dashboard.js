import React, { Component } from 'react';
import axios from '../config/axios';
import 'antd/dist/antd.css';
import { Form, Card, Descriptions, Table, Modal, Tag, Row, Col, Icon, Input, Button, Progress, message, Alert, Spin } from 'antd';
import Highlighter from 'react-highlight-words';
import './index.css';

class Dashboard extends Component {
  
    componentDidMount = () => {

      this.setState({
            data : [],
            count : [],
            summery : [],
      })

      if(localStorage.getItem("header") !== null){ 
        axios.get('leave_count/summery/'+this.parseJwt(localStorage.getItem("header")), 
          {
              headers: {
                  Authorization: 'Bearer ' + localStorage.getItem("header")
              }
          })
          .then(res => {
              if (res.data.success === true) {
                this.setState({
                  summery : res.data.list,
                  sumRes : true
                })
                console.log("Summary", res.data.list)
              } else {
                    message.error(res.data.message);
              }
          })
          .catch(e => {
            message.error("Something went wrong");
            console.log(e);
          })  

        axios.get('leave/find/'+this.parseJwt(localStorage.getItem("header")), 
          {
              headers: {
                  Authorization: 'Bearer ' + localStorage.getItem("header")
              }
          })
          .then(res => {
            if (res.data.success === true) {  
                this.setState({
                    data : (res.data.list).reverse(),
                    dataRes : true
                })
            } else {
              message.error(res.data.message);
            }
          })
          .catch(e => {
            message.error("Something went wrong");
            console.log(e);
          })  
          
        axios.get('user/get/'+this.parseJwt(localStorage.getItem("header")), 
          {
              headers: {
                  Authorization: 'Bearer ' + localStorage.getItem("header")
              }
          })
          .then(res => {
              if (res.data.success === true) {  
                this.setState({
                  employee : res.data.employee,
                  empRes : true
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
    } 

    parseJwt (token) {
      var base64Url = token.split('.')[1];
      var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
  
      return JSON.parse(jsonPayload).sub;
    };

    state = {
        data : [],
        leave : [],
        employee : [],
        summery : [],
        sumRes : false,
        dataRes : false,
        empRes : false
    }

    showModal = (leave) => {
        this.setState({
          visible: true,
          leave: leave
        });
      };
    
      handleOk = e => {
        console.log(e);
        this.setState({
          visible: false,
        });
    };

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

      handleSearch = (selectedKeys, confirm) => {
        confirm();
        this.setState({ searchText: selectedKeys[0] });
      };
    
      handleReset = clearFilters => {
        clearFilters();
        this.setState({ searchText: '' });
      };

    render() {

        const {data, leave, employee, summery, sumRes, empRes, dataRes} = this.state;
      
        const summeryCol = [
          {
            title: 'Type',
            key: '0',
            dataIndex: 'type'
          },
          {
            title: 'Entitlement',
            key: '1',
            dataIndex: 'entitlement',
            align: 'center'
          },    
          {
            title: 'Utilized',
            key: '2',
            dataIndex: 'utilized',
            align: 'center'
          },
          {
            title: 'Remaining',
            key: '3',
            dataIndex: 'remaining',
            align: 'center'
          }
        ]; 

        const columns = [ 
            
            {
                title: 'Date of request',
                dataIndex: 'requestDateTime',
                ...this.getColumnSearchProps('requestDateTime')
            },
            {
              title: 'Leave Type',
              dataIndex: 'type',
              // render: type => (
              //  type.type
              // )
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
                dataIndex: 'number_of_leave_days',
                align: 'center'
            },
            {
                title: 'Status',
                dataIndex: 'status',
                ...this.getColumnSearchProps('status'),

                render: tag => {
                    let color;
                    if (tag === 'Pending') {
                      color = 'geekblue';
                    }else if (tag === 'Approved') {
                      color = 'green';
                    }else {
                      color = 'red';
                    }
                    return (
                      <Tag style={{width:"70px"}} color={color} key={tag}>
                        {tag}
                      </Tag>
                    );
                  }
            },
            {
              dataIndex: '',
              key: 'z',
              width: '1%',
              render: (emp) => <Icon type="info-circle" onClick={this.showModal.bind(this, emp)}  theme="twoTone" />,
            }
        ];

        return (
            <div >
            {sumRes && dataRes && empRes? 
            
            <div >
              <Card hoverable='true'>  
                <Row gutter={16}>
                  <center> 
                  <Col span={6}>
                    <Progress type="circle" percent={this.state.summery[0].utilized / this.state.summery[0].entitlement * 100} format={percent => `${this.state.summery[0].utilized} / ${this.state.summery[0].entitlement}`}/>
                    <br/>
                    <Tag color="volcano" style={{width:"70px"}}>Casual</Tag>
                  </Col>
                  <Col span={6}>
                    <Progress type="circle" percent={this.state.summery[1].utilized / this.state.summery[1].entitlement * 100} format={percent => `${this.state.summery[1].utilized} / ${this.state.summery[1].entitlement}`}/>
                    <br/>
                    <Tag color="volcano" style={{width:"70px"}}>Medical</Tag>
                  </Col> 
                  <Col span={6}>
                    <Progress type="circle" percent={this.state.summery[2].utilized / this.state.summery[2].entitlement * 100} format={percent => `${this.state.summery[2].utilized} / ${this.state.summery[2].entitlement}`}/>
                    <br/>
                    <Tag color="volcano" style={{width:"70px"}}>Annual</Tag>
                  </Col>
                  <Col span={6}>
                    <Progress type="circle" percent={this.state.summery[3].utilized / this.state.summery[3].entitlement * 100} format={percent => `${this.state.summery[3].utilized} / ${this.state.summery[3].entitlement}`}/>
                    <br/>
                    <Tag color="volcano" style={{width:"70px"}}>Lieu</Tag>
                  </Col>
                  </center>
                </Row>
              </Card>
              <br/>
              <Row gutter={16}>
                <Col span={9} > 
                  <Card hoverable='true' className="card-height">
                    <h3>Leave Summery</h3>
                    <Table rowKey={record => record.type} columns={summeryCol} dataSource={summery} size="small" pagination={false}/>
                  </Card>
                </Col>
                <Col span={15}>  
                  <Card hoverable='true'>
                    <h3>Employee Infomation</h3>
                    {employee.length !== 0 ?
                    <Descriptions size="small" bordered column={2} >
                      <Descriptions.Item label="Emp Number">{employee.userId}</Descriptions.Item>
                      <Descriptions.Item label="NIC" span={1}>{employee.nic}</Descriptions.Item>
                      <Descriptions.Item label="Email" >{employee.email}</Descriptions.Item>
                      <Descriptions.Item label="Contact No" span={1}> {employee.contact}</Descriptions.Item>
                      <Descriptions.Item label="Date of Birth">{employee.dob}</Descriptions.Item>
                      <Descriptions.Item label="Religion" span={1}>{employee.religion}</Descriptions.Item>
                      <Descriptions.Item label="Residential Address" span={2}>{employee.residence}</Descriptions.Item>
                      <Descriptions.Item label="Permanent Address" span={2}>{employee.permanent}</Descriptions.Item>
                      <Descriptions.Item label="Supervisor 01">{employee.supervisor1}</Descriptions.Item>
                      <Descriptions.Item label="Supervisor 02" span={1}>{employee.supervisor2}</Descriptions.Item>
                      <Descriptions.Item label="Join Date">{employee.joinDate}</Descriptions.Item>
                      { employee.role === "Intern" ?
                      <Descriptions.Item label="End Date">{employee.confirmDate}</Descriptions.Item>
                      : 
                      <Descriptions.Item label="Confirm Date">{employee.confirmDate}</Descriptions.Item>
                      }
                    </Descriptions> : null}
                  </Card>
                </Col>
            </Row>
                <br/>
              
                <Card hoverable='true' >
                    <Table rowKey={record => record.id} columns={columns} dataSource={data}  pagination={{ pageSize: 5 }} size="middle" />
                </Card>      
                
            </div >      
                : 
                <div className="example">
                  <Spin size="large" />
                </div>
                }          

                {leave.length!==0? <Modal
                  title="Full Leave Record"
                  visible={this.state.visible}
                  onOk={this.handleOk}
                  onCancel={this.handleOk}
                  footer={null}
                >
                  { 
                    leave.status === "Approved"?
                      <Alert message="Your request was approved" type="success" style={{width:'460px'}}/>
                    : leave.status === "Pending"?
                      <Alert message="Your request is still Pending" type="info" style={{width:'460px'}}/>
                    : <Alert message="Your request was rejected" type="error" style={{width:'460px'}}/>
                  }
                  <br/>
                  <br/>
                  <Row>
                    <Col span={8}>
                    Date of Request: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'145px'}}>{leave.requestDateTime}</Tag>
                    </Col>
                    <Col span={8}>
                    Leave Type: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'145px'}}>{leave.type}</Tag>
                    </Col>
                    <Col span={8}>
                    Number Of Days: 
                    <Tag color="volcano" style={{width:'145px'}}>{leave.number_of_leave_days}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  { leave.number_of_leave_days !== 0.5 ?
                  <Row>
                    <Col span={6}>
                    Start Date: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'105px'}}>
                        {leave.startDate} 
                      </Tag>
                    </Col>
                    <Col span={6}>
                    Full/ Half: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'105px'}}> 
                      {leave.startHalf}
                    </Tag>
                    </Col>
                    <Col span={6}>
                    End Date: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'105px'}}>
                        {leave.endDate} 
                      </Tag>
                    </Col>
                    <Col span={6}>
                    Full/ Half: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'105px'}}>
                      {leave.endHalf}
                    </Tag>
                    </Col>
                  </Row>
                  : 
                  <Row>
                    <Col span={12}>
                    Leave Date: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'225px'}}>
                        {leave.startDate} 
                      </Tag>
                    </Col>
                    <Col span={12}>
                    Leave Half: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'225px'}}> 
                      {leave.startHalf}
                    </Tag>
                    </Col>
                  </Row> 
                  }
                  <br/>
                  <Row>
                    <Col span={12}>
                    Checked By: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'225px'}}>{leave.checkBy}</Tag>
                    </Col>
                    <Col span={12}>
                    Checked On: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'225px'}}>{leave.checkTime}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={24}>
                    Duty Cover By:
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'460px'}}>{leave.duty}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={24}>
                    Special Note: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'460px', whiteSpace:'normal'}}>{leave.specialNotes}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  { 
                    leave.status === "Rejected"?                  
                      <p>Reason For Reject: <br/><Tag color="volcano" style={{width:'460px', whiteSpace:'normal'}}>{leave.reject}</Tag></p>
                    : null
                  }
                </Modal>: null}
              
            </div>
        )
    }
}    

const Dash = Form.create({name: "dashboard"})(Dashboard);

export default Dash;