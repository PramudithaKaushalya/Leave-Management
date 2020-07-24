import React, { Component } from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import {Table, Button, Icon, Input, Card, Modal, Tag, message, Alert, Row, Col, Spin } from 'antd';
import Highlighter from 'react-highlight-words';
import './index.css';

const { confirm } = Modal;

class ReadLeaves extends Component {
    
    componentWillMount(){
       this.reload();
    } 

    state = {
        mounted : false,
        searchText: '',
        data : [],
        visible: false,
        leave : [] 
    };
    
    reload () {
      this.setState({
        data : [],
        mounted : false
      })
    
      axios.get('leave/all', 
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
        console.log(e);
      })  

      this.setState({
        mounted : true
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
    
      handleSearch = (selectedKeys, confirm) => {
        confirm();
        this.setState({ searchText: selectedKeys[0] });
      };
    
      handleReset = clearFilters => {
        clearFilters();
        this.setState({ searchText: '' });
      };   

    

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

    showConfirm = (leave) =>{
      confirm({
        title: 'Do you want to remove this request ?',
        content: 'Make sure you can\'t delete pending leave request.',
        okText: 'Remove',
        okType: 'danger',
        onOk: () => {
          axios.get('leave/delete/'+leave.id, 
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
              message.error("Something Went Wrong");
          })
        },
        onCancel() {
        },
      });
    }

    render() {
       
        const {leave} = this.state;

        const columns = [
            {
              title: 'Name',
              dataIndex: 'user',
              ...this.getColumnSearchProps('user')
            },  
            {
              title: 'Leave Type',
              dataIndex: 'type',
              // render: type => (
              //  type.type
              // ),
              ...this.getColumnSearchProps('type')
            },
            {
                title: 'Start Date',
                dataIndex: 'startDate',
                // ...this.getColumnSearchProps('startDate')
            },
            {
                title: 'End Date',
                dataIndex: 'endDate',
                ...this.getColumnSearchProps('endDate')
            },
            {
              title: 'Supervisor 01',
              dataIndex: 'supervisor1',
              // ...this.getColumnSearchProps('supervisor1'),
            },
            {
                title: 'Date of request',
                dataIndex: 'requestDateTime',
                ...this.getColumnSearchProps('requestDateTime'),
            },
            {
              title: 'Checked By',
              dataIndex: 'checkBy',
              ...this.getColumnSearchProps('checkBy'),
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
                      },
            },
            {
              dataIndex: '',
              key: 'z',
              width: '1%',
              render: (emp) => <Icon type="info-circle" onClick={this.showModal.bind(this, emp)}/>,
            },
            {
              dataIndex: '',
              key: 'y',
              width: '3%',
              render: (emp) => <Icon type="delete" onClick={this.showConfirm.bind(this, emp)}  theme="twoTone" twoToneColor='#EE204D'/>,
            },

          ];

        return (
            <div>
            { this.state.mounted? 
              <Card type="inner" title="Leave Requests" hoverable='true'>
                <Table rowKey={record => record.id} columns={columns} dataSource={this.state.data}  pagination={{ pageSize: 10 }} size="middle" />
              </Card> 
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
                      <Alert message="Approved request" type="success" style={{width:'460px'}}/>
                    : leave.status === "Pending"?
                      <Alert message="Pending request" type="info" style={{width:'460px'}}/>
                    : <Alert message="Rejected request" type="error" style={{width:'460px'}}/>
                  }
                  <br/>
                  <Row>
                    <Col span={12}>
                    Name of employee:  
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'225px'}}>{leave.user}</Tag>
                    </Col>
                    <Col span={12}>
                    Duty Cover By:
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'225px'}}>{leave.duty}</Tag>
                    </Col>
                  </Row>
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
                    Start Time: 
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
                    End Time: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'105px'}}>
                      {leave.endHalf}
                    </Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={12}>
                    Supervisor 01: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'225px'}}>{leave.supervisor1}</Tag>
                    </Col>
                    <Col span={12}>
                    Supervisor 02:
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'225px'}}>{leave.supervisor2}</Tag>
                    </Col>
                  </Row>
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
                    Special Note: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'460px'}}>{leave.specialNotes}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  { 
                    leave.status === "Rejected"?                  
                      <p>Reason For Reject: <br/><Tag color="volcano" style={{width:'460px'}}>{leave.reject}</Tag></p>
                    : null
                  }
                </Modal>: null}
            </div>
        )
    }
}    


export default ReadLeaves;