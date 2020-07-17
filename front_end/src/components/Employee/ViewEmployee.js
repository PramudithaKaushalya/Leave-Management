import React, { Component } from 'react';
import axios from '../../config/axios';
import 'antd/dist/antd.css';
import {Table, Button, Icon, Input, Drawer, Card, Tag, Modal, message, Form, Alert, Row, Col, Spin } from 'antd';
import Highlighter from 'react-highlight-words';
import UpdateEmployee from '../Employee/UpdateEmployee';
import AddEmployee from '../Employee/AddEmployee';
import './index.css';

const ResignForm = Form.create({ name: 'viewEmployee' })(

  class extends React.Component {
    render() {
      const { visible, onCancel, onCreate, form } = this.props;
      const { getFieldDecorator } = form;
      return (
        <Modal
          visible={visible}
          title="Resign the employee"
          okText="Resign"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form layout="vertical">
            <Form.Item label="Resign Date">
              {getFieldDecorator('date', {
                rules: [
                  { 
                    required: true, 
                    message: 'Please input the resign date!' 
                  },
                  {
                    message: 'Input valid date as example!',
                    len : 10
                  }
                ],
              })(<Input type="textarea" placeholder="ex: 2020-01-01"/>)}
            </Form.Item>
          </Form>
        </Modal>
      );
    }
  },
);

function convertArrayOfObjectsToCSV(args) {
  var result, ctr, keys, columnDelimiter, lineDelimiter, data;

  data = args.data || null;
  if (data == null || !data.length) {
      return null;
  }

  columnDelimiter = args.columnDelimiter || ',';
  lineDelimiter = args.lineDelimiter || '\n';

  keys = Object.keys(data[0]);

  result = '';
  result += keys.join(columnDelimiter);
  result += lineDelimiter;

  data.forEach(function(item) {
      ctr = 0;
      keys.forEach(function(key) {
          if (ctr > 0) result += columnDelimiter;

          result += item[key];
          ctr++;
      });
      result += lineDelimiter;
  });

  return result;
}

class ViewEmployee extends Component {
    
    componentWillMount(){
       this.reload();
    } 

    reload() {
      this.setState({
        data : []
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
              data : res.data.list
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
        searchText: '',
        data : [],
        visibleUpdate: false,
        visibleAdd: false,
        emp_id: undefined,
        updateEmployee: [],
        emp : [],
        visible: false,
        visibleResign : false,
        empId: null 
    };
    
    info = (emp) => {
      this.setState({
        visible: true,
        emp: emp
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
    
      showAddDrawer = () => {
        this.setState({
            visibleAdd: true
          });
      }

      onAddClose = () => {
          this.setState({
            visibleAdd: false
          });
          this.reload();
      };

      showUpdateDrawer = (emp) => {
        
  
        axios.get('user/get/'+ emp.id, 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if (res.data.success === true) {  
              this.setState({
                  updateEmployee : res.data.employee,
                  visibleUpdate: true
              })
              
              console.log("Update response",res.data.employee);
            } else {
              message.error(res.data.message);
            }
        })
        .catch(e => {
          message.error("Something went wrong");
        }) 
      };
    
    onUpdateClose = () => {
      console.log("Update close");
        this.setState({
          visibleUpdate: false,
        });
        this.reload();
    };
    
    showModal = (emp) => {
      this.setState({ 
        visibleResign : true,
        emp_id : emp.id
      });
    };

    saveFormRef = formRef => {
      this.formRef = formRef;
    };

    handleResign = () => {
      const { form } = this.formRef.props;
      form.validateFields((err, values) => {
        console.log(values)
        if (!err) {
          axios.post('user/resign', 
                {
                  id: this.state.emp_id,
                  resignDate:values.date
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
                    message.error(err.response.data.error);
                })
          this.setState({ visibleResign : false });
        }
        
      });
    };
    
    handleCancel = () => {
      const { form } = this.formRef.props;
      form.resetFields();  
      this.setState({ 
        visibleResign: false
      });
    };

    download = (args) => {
      var data, filename, link;

      var csv = convertArrayOfObjectsToCSV({
          data: this.state.data
      });
      if (csv == null) return;

      filename = args.filename || 'employees.csv';

      if (!csv.match(/^data:text\/csv/i)) {
          csv = 'data:text/csv;charset=utf-8,' + csv;
      }
      data = encodeURI(csv);

      link = document.createElement('a');
      link.setAttribute('href', data);
      link.setAttribute('download', filename);
      link.click();
    }

    render() {
       
        const {emp} = this.state;      
        const columns = [
            {
              title: 'Id',
              dataIndex: 'userId',
              ...this.getColumnSearchProps('userId'),
            },
            {
              title: 'Name',
              dataIndex: 'firstName',
              ...this.getColumnSearchProps('firstName'),
            },
            {
              title: 'Email',
              dataIndex: 'email',
            },
            {
                title: 'Contact',
                dataIndex: 'contact',
            },
            {
              title: 'Role',
              dataIndex: 'role',
              ...this.getColumnSearchProps('role')
            },
            {
              title: 'Department',
              dataIndex: 'department',
              ...this.getColumnSearchProps('department'),
              // render: role => (
              //  role.department_name
              // )
            },
            {
                title: 'Supervisor 01',
                dataIndex: 'supervisor1',
                ...this.getColumnSearchProps('supervisor1'),
            },
            {
              title: 'Status',
              dataIndex: 'status',
              ...this.getColumnSearchProps('status'),
              render: tag => {
                let color;
                if (tag === 'Resign') {
                  color = 'red';
                }else {
                  color = 'green';
                }
                return (
                  <Tag color={color} key={tag}>
                    {tag}
                  </Tag>
                );
              }
            },
            {
              dataIndex: '',
              key: 'z',
              width: '3%',
              render: (emp) => <Icon type="info-circle" onClick={this.info.bind(this, emp)}/>,
            },
            {
                dataIndex: '',
                key: 'x',
                width: '3%',
                render: (emp) => <Icon type="edit" onClick={this.showUpdateDrawer.bind(this, emp)}  theme="twoTone" />,
            },
            {
                dataIndex: '',
                key: 'y',
                width: '3%',
                render: (emp) => <Icon type="close-circle" onClick={this.showModal.bind(this, emp)}  theme="twoTone" twoToneColor='#EE204D'/>,
            },

          ];

        return (
            <div>
            { this.state.data.length !== 0 ? 
              <Card hoverable='true'>

                <Row>
                  <Col span={22}>
                  </Col>
                  <Col span={1}>
                    <Button  style={{float:'right'}} type="primary" icon="download"  shape="circle" onClick={this.download.bind(this,{ filename: "Employees.csv" })}/>
                  </Col>
                  <Col span={1}>
                    <Button style={{float:'right'}} type="primary" onClick={this.showAddDrawer} shape="circle" icon="user-add" />
                  </Col>
                </Row>

                <br/><br/>
                <Table rowKey={record => record.id} columns={columns} dataSource={this.state.data}  pagination={{ pageSize: 10 }} size="middle" />

                <Drawer
                title="Update Existing Employee"
                width={570}
                onClose={this.onUpdateClose}
                visible={this.state.visibleUpdate}
                >
                    <UpdateEmployee update={this.state.updateEmployee} close={this.onUpdateClose} />
                </Drawer>

                <Drawer
                title="Add New Employee"
                width={570}
                onClose={this.onAddClose}
                visible={this.state.visibleAdd}
                >
                    <AddEmployee close={this.onAddClose}/>
                </Drawer>
              </Card> 
              : 
              <div className="example">
                <Spin size="large" />
              </div>
              }  
                {emp.length!==0? <Modal
                  title="Full Employee Detail"
                  visible={this.state.visible}
                  onOk={this.handleOk}
                  onCancel={this.handleOk}
                  footer={null}
                >
                  
                  { 
                    emp.status === "Working"?
                      <Alert message="Currently Working" type="success" style={{width:'456px'}}/>
                    :
                      <Alert message="Currently Resign" type="error" style={{width:'456px'}}/>
                  }
                  <br/>
                  <Row>
                    <Col span={6}>
                      Emp No: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'100px'}}>{emp.userId}</Tag>
                    </Col>
                    <Col span={12}>
                      Name: 
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;
                    <Tag color="volcano" style={{width:'220px'}}>{emp.firstName} {emp.secondName}</Tag>
                    </Col>
                    <Col span={6}>
                      Role: <Tag color="volcano" style={{width:'100px'}}>{emp.role}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={10}>
                      Department: <Tag color="volcano" style={{width:'180px'}}>{emp.department}</Tag>
                    </Col>
                    <Col span={14}>
                      Designation: <Tag color="volcano" style={{width:'260px'}}>{emp.designation}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={18}>
                      Email: <Tag color="volcano" style={{width:'335px'}}>{emp.email}</Tag>
                    </Col>
                    <Col span={6}>
                      Contact: <Tag color="volcano" style={{width:'100px'}}>{emp.contact}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={16}>
                      Address: <Tag color="volcano" style={{width:'455px'}}>{emp.residence}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={12}>
                      Supervisor 01: <Tag color="volcano" style={{width:'220px'}}>{emp.supervisor1}</Tag>
                    </Col>
                    <Col span={12}>
                      Supervisor 02: <Tag color="volcano" style={{width:'220px'}}>{emp.supervisor2}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={8}>
                      Join Date: 
                      &nbsp;&nbsp;&nbsp;
                      &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'140px'}}>{emp.joinDate}</Tag>
                    </Col>
                    <Col span={8}>
                      Confirm Date:
                      &nbsp;&nbsp;&nbsp;
                      &nbsp;&nbsp;&nbsp; 
                      <Tag color="volcano" style={{width:'140px'}}>{emp.confirmDate}</Tag>
                    </Col> 
                    <Col span={8}>
                      Resign Date:
                      &nbsp;&nbsp;&nbsp;
                      &nbsp;&nbsp;&nbsp; 
                      <Tag color="volcano" style={{width:'140px'}}>{emp.resignDate}</Tag>
                    </Col>
                  </Row>
                  <br/>
                  <Row>
                    <Col span={8}>
                      Annual: 
                      &nbsp;&nbsp;&nbsp;
                      &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'140px'}}>{emp.annual}</Tag> 
                    </Col>
                    <Col span={8}>
                      Casual: 
                      &nbsp;&nbsp;&nbsp;
                      &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'140px'}}>{emp.casual}</Tag>
                    </Col>
                    <Col span={8}>
                      Medical:
                      &nbsp;&nbsp;&nbsp;
                      &nbsp;&nbsp;&nbsp;
                      <Tag color="volcano" style={{width:'140px'}}>{emp.medical}</Tag>
                    </Col>
                  </Row>
                  <br/>
                </Modal>: null}

                  
                <ResignForm
                  wrappedComponentRef={this.saveFormRef}
                  visible={this.state.visibleResign}
                  onCancel={this.handleCancel}
                  onCreate={this.handleResign}
                />

            </div>
        )
    }
}    

const WrappedViewEmployee = Form.create({ name: 'viewEmployee' })(ViewEmployee);

export default WrappedViewEmployee;