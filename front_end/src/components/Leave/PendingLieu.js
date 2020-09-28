import React, { Component } from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import {Table, Icon, Card, Modal, message, Spin, Row, Col, Tag } from 'antd';
import './index.css';
import moment from 'moment';

const { confirm } = Modal;

class PendingLieu extends Component {
    
    componentWillMount(){
      this.reload();
    } 

    state = {
        pendingLieus: [],  
        leave : [],
        mounted : false, 
        reject: null,
        visibleAccept: false,
        spinning : false
    };
    
    async reload () {
        this.setState({
            mounted : false
        })
        
        await axios.get('lieu_leave/get_all', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success) {              
                this.setState({
                    pendingLieus : res.data.list
                })
            } else {
                message.error(res.data.message);
            }
        })
        .catch(e => {
            message.error("Something went wrong");
            console.log(e.response.data.error);
        })  

        this.setState({
            mounted : true
        })
    }

    showConfirm = (leave) =>{
      confirm({
        title: 'Are you sure to reject this lieu leave?',
        okText: 'Reject',
        okType: 'danger',
        onOk: () => {
          this.setState({
            spinning : true
          });
          axios.get('lieu_leave/reject/'+leave.id, 
          {
              headers: {
                  Authorization: 'Bearer ' + localStorage.getItem("header")
              }
          })
          .then(res => {
              if (res.data.success === true) {
                message.success(res.data.message);
                this.reload();
                this.setState({
                  spinning : false
                });
              } else {
                message.error(res.data.message);
                this.setState({
                  spinning : false
                });
              }
          }).catch( err => {
              console.log(err);
              message.error("Something Went Wrong!");
              this.setState({
                spinning : false
              });
          })
        },
        onCancel() {
        },
      });
    }

    handleCancel = () => {
        this.setState({ 
          visibleAccept: false
        });
    };

    saveFormRef = formRef => {
        this.formRef = formRef;
    };

    show = (leave) => {
        console.log(leave)
        this.setState({
          leave: leave,
          visibleAccept: true
        });
    };

    handleOk = () => {
      
        this.setState({
          confirmLoading: true,
          spinning: true
        });
        
        setTimeout(() => {
          this.setState({
            visibleAccept: false,
            confirmLoading: false,
          });
        }, 500);
  
        axios.get('lieu_leave/approve/'+this.state.leave.id, 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {
              message.success(res.data.message);
              this.reload();
              this.setState({
                spinning : false
              });
            } else {
              message.error(res.data.message);
              this.setState({
                spinning : false
              });
            }
        }).catch( err => {
            console.log(err);
            message.error("Something Went Wrong!");
            this.setState({
              spinning : false
            });
        })
    };
    
    render() {

        const { visibleAccept, confirmLoading, leave, pendingLieus, spinning } = this.state;
        const columns = [
            {
                title: 'Emoloyee',
                key: '0',
                dataIndex: 'employee'
            },
            {
                title: 'Requested Time',
                key: '1',
                dataIndex: 'requestAt',
                render: date => {
                  
                  return (
                    moment(date, "YYYY-MM-DD HH:mm Z").format('lll')
                  );
                }
            },
            {
                title: 'Worked Date',
                key: '2',
                dataIndex: 'date',
                render: date => {
                  
                  return (
                    moment(date).format('YYYY-MM-DD')
                  );
                }
            },
            {
                title: 'Period',
                key: '3',
                dataIndex: 'period',
                render: tag => {
                    let text;
                    if (tag === 0) {
                        text = 'Half Day';
                    }else {
                        text = 'Full Day';
                    }
                    return (
                        text
                    );
                }
            },    
            {
                title: 'Project',
                key: '4',
                dataIndex: 'project'
            },     
            {
              key: '5',
              width: '3%',
              render: (emp) => <Icon type="like" onClick={this.show.bind(this, emp)}  theme="twoTone" twoToneColor="#81CF7F"/>,
            },
            {
              dataIndex: '',
              key: '6',
              width: '1%',
              render: (emp) => <Icon type="dislike" onClick={this.showConfirm.bind(this, emp)} theme="twoTone" twoToneColor="red"/>,
            },

          ];

        return (
          <div>
            <Spin tip="Waiting..." spinning={spinning}>
            { this.state.mounted ? 
              <Card type="inner" title="Pending Lieu Leaves" hoverable='true'>
                <Table rowKey={record => record.id} columns={columns} dataSource={pendingLieus}  pagination={{ pageSize: 10 }} size="middle" />
              </Card> 
            : 
            <div className="example">
              <Spin size="large" />
            </div>
            } 

            { (this.state.leave.length!==0) ? 
                <Modal
                    title="Accept the lieu leave request."
                    visible={visibleAccept}
                    onOk={this.handleOk}
                    confirmLoading={confirmLoading}
                    onCancel={this.handleCancel}
                    okText="Approve"
                >
                  <div>
                  <Row>
                    <Col span={12}>
                    Name of employee:  
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'225px'}}>{leave.employee}</Tag>
                    </Col>
                    <Col span={12}>
                    Project:
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'225px'}}>{leave.project}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={12}>
                    Date: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'225px'}}>{moment(leave.date).format('YYYY-MM-DD')}</Tag>
                    </Col>
                    <Col span={12}>
                    Full/ Half:
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    { leave.period === 0 ?
                    <Tag color="volcano" style={{width:'225px'}}>Half Day</Tag>
                    :
                    <Tag color="volcano" style={{width:'225px'}}>Full Day</Tag>
                    }
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={24}>
                    Works Done: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'460px', whiteSpace:'normal'}}>{leave.worksDone}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  </div>
                </Modal> : null}

              </Spin>
          </div>
        )
    }
}    

export default PendingLieu;