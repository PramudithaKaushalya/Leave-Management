import React, { Component } from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import {Table, Icon, Card, Modal, message, Spin } from 'antd';
import './index.css';
import moment from 'moment';

const { confirm } = Modal;

class PendingLieu extends Component {
    
    componentWillMount(){
       this.reload();
    } 

    state = {
        visible: false,
        leave : [],
        mounted : false, 
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
                    leave : res.data.list
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
        title: 'Are you sure to approve this lieu leave?',
        okText: 'Approve',
        okType: 'success',
        onOk: () => {
          axios.get('lieu_leave/approve/'+leave.id, 
          {
              headers: {
                  Authorization: 'Bearer ' + localStorage.getItem("header")
              }
          })
          .then(res => {
              if (res.data.success === true) {
                message.success(res.data.message);
                this.reload();
              } else {
                message.error(res.data.message);
              }
          }).catch( err => {
              console.log(err);
              message.error("Something Went Wrong!");
          })
        },
        onCancel() {
        },
      });
    }

    render() {

        const columns = [
            {
                title: 'Date',
                key: '0',
                dataIndex: 'date',
                render: date => {
                  
                  return (
                    moment(date, "YYYY-MM-DD HH:mm Z").format('lll')
                  );
                }
            },
            {
                title: 'Period',
                key: '1',
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
                key: '2',
                dataIndex: 'project'
            }, 
            {
                title: 'Works done',
                key: '2',
                dataIndex: 'worksDone'
            },    
            {
              dataIndex: '',
              key: 'y',
              width: '3%',
              render: (emp) => <Icon type="like" onClick={this.showConfirm.bind(this, emp)}  theme="twoTone" />,
            },

          ];

        return (
            <div>
            { this.state.mounted ? 
              <Card title="Leave Requests" hoverable='true'>
                <Table rowKey={record => record.id} columns={columns} dataSource={this.state.leave}  pagination={{ pageSize: 7 }} size="middle" />
              </Card> 
            : 
            <div className="example">
              <Spin size="large" />
            </div>
            } 
            </div>
        )
    }
}    


export default PendingLieu;