import React, { Component } from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import {Table, Button, Icon, Input, Card, Modal, Form, Tag, message, Row, Col, Spin } from 'antd';
import Highlighter from 'react-highlight-words';
import './index.css';

const CollectionCreateForm = Form.create({ name: 'pendingLeaves' })(

  class extends React.Component {
    render() {
      const { visible, onCancel, onCreate, form } = this.props;
      const { getFieldDecorator } = form;
      return (
        <Modal
          visible={visible}
          title="Reject the request"
          okText="Reject"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form layout="vertical">
            <Form.Item label="Reason">
              {getFieldDecorator('reason', {
                rules: [{ required: true, message: 'Please input the reason for reject!' }],
              })(<Input type="textarea"/>)}
            </Form.Item>
          </Form>
        </Modal>
      );
    }
  },
);

class PendingLeaves extends Component {
    
    componentWillMount(){
        this.reload();
    } 

    reload(){
      this.setState({
        data : []
      })

      axios.get('leave/pending', 
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

      axios.get('leave_count/profile', 
      {
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem("header")
          }
      })
      .then(res => {
          if (res.data.success === true) {
            this.setState({
                count : res.data.list
            })
          } else {
              message.error(res.data.message);
          }
      })
      .catch(e => {
        message.error("Something went wrong");
        console.log(e.response.data.error);
      }) 

      axios.get('user/all', 
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

    state = {
        visible: false,
        searchText: '',
        data : [],
        reject: null,
        visibleAccept: false,
        confirmLoading: false,
        leave: [],
        count: [],
        countOfOne: [],
        employees : [],
        employee : []
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

    show = (leave) => {

      this.state.count.map(item => (
        item.name===leave.user ? this.setState({ countOfOne : item }) : null
      ))

      this.state.employees.map(item => (
        item.firstName+" "+item.secondName === leave.user ? this.setState({ employee : item }) : null
      ))

      this.setState({
        leave: leave,
        visibleAccept: true
      });
    };
  
    handleOk = () => {
      
      this.setState({
        confirmLoading: true,
      });
      
      setTimeout(() => {
        this.setState({
          visibleAccept: false,
          confirmLoading: false,
        });
      }, 500);

      if(this.state.leave.type === 'Casual' && this.state.leave.number_of_leave_days > (this.state.employee.casual-this.state.countOfOne.casual)){
        message.error("Exceed casual limit");
      }else if(this.state.leave.type === 'Medical' && this.state.leave.number_of_leave_days > (this.state.employee.medical-this.state.countOfOne.medical)){
        message.error("Exceed medical limit");
      }else if(this.state.leave.type === 'Maternity' && this.state.leave.number_of_leave_days > (84-this.state.countOfOne.maternity)){
        message.error("Exceed maternity limit");
      }else if(this.state.leave.type === 'Paternity' && this.state.leave.number_of_leave_days > (3-this.state.countOfOne.paternity)){
        message.error("Exceed paternity limit");
      }else if(this.state.leave.type === 'Annual' && this.state.leave.number_of_leave_days > (this.state.employee.annual-this.state.countOfOne.annual)){
        message.error("Exceed annual limit");  
      }else { 
        axios.get('leave/approve/'+this.state.leave.id, 
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
                      console.log(err.response.data.error);
                      message.error("Something went wrong");
                  })
      }
    };

    showModal = (leave) => {
      
      this.setState({ 
        visible: true,
        reject: leave.id
      });
    };
  
    handleCancel = () => {
      const { form } = this.formRef.props;
      form.resetFields();  
      this.setState({ 
        visible: false,
        visibleAccept: false
      });
    };
  
    handleReject = () => {
      const { form } = this.formRef.props;        
      form.validateFields((err, values) => {
        if (!err) {
          
          axios.post('leave/reject/'+this.state.reject, 
                {
                  reject:values.reason
                },
                {
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem("header")
                    }
                })
                .then(res => {
                    if (res.data.success === true) {
                      message.success(res.data.message); 
                      form.resetFields();
                      this.reload(); 
                    } else {
                      message.error(res.data.message);
                    }
                }).catch( err => {
                    console.log(err.response.data.error);
                    message.error("Something went wrong");
                })
          this.setState({ visible: false });
        }
      });
    };
  
    saveFormRef = formRef => {
      this.formRef = formRef;
    };

    render() {
      const { visibleAccept, confirmLoading, leave } = this.state;
        const columns = [
            {
              title: 'Name',
              key: '0',
              dataIndex: 'user',
              ...this.getColumnSearchProps('user'),
            },  
            {
              title: 'Type',
              key: '1',
              dataIndex: 'type',
              ...this.getColumnSearchProps('type'),
            },
            {
              title: 'Start Date',
              key: '2',
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
                title: 'Date of request',
                dataIndex: 'requestDateTime',
            },
            {
              title: 'Supervisor 01',
              dataIndex: 'supervisor1',
              ...this.getColumnSearchProps('supervisor1'),
            },
            {
              title: 'Supervisor 02',
              dataIndex: 'supervisor2',
              ...this.getColumnSearchProps('supervisor2'),
            },
            {
                dataIndex: '',
                key: 'b',
                width: '1%',
                render: (emp) => <Icon type="like" onClick={this.show.bind(this, emp)} theme="twoTone" />,
            },
            {
              dataIndex: '',
              key: 'c',
              width: '1%',
              render: (emp) => <Icon type="dislike" onClick={this.showModal.bind(this, emp)} theme="twoTone" />,
          },

          ];

        return (
            <div>
            { this.state.data.length !== 0? 
              <Card title="Pending Leaves" hoverable='true'>
                <Table rowKey={record => record.id} columns={columns} dataSource={this.state.data}  pagination={{ pageSize: 7 }} size="middle" />
              </Card> 
             : 
             <div className="example">
               <Spin size="large" />
             </div>
             }  

                <CollectionCreateForm
                  wrappedComponentRef={this.saveFormRef}
                  visible={this.state.visible}
                  onCancel={this.handleCancel}
                  onCreate={this.handleReject}
                />

                { (leave.length!==0) ? <Modal
                title="Accept the leave request."
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
                    <Col span={24}>
                    Special Note: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'460px'}}>{leave.specialNotes}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  </div>
                </Modal> : null}
            </div>
        )
    }
}    


const WrappedPendingLeaves = Form.create({ name: 'pendingLeaves' })(PendingLeaves);

export default WrappedPendingLeaves;