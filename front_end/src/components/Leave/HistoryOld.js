import React, { Component } from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import {Table, Button, Icon, Input, Card, Modal, Tag, Select, Row, Col, DatePicker, Form, message, Alert } from 'antd';
import Highlighter from 'react-highlight-words';

const { Option } = Select;
const { RangePicker } = DatePicker;

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

class History extends Component {
    
    componentWillMount(){
       this.reload();
    } 

    state = {
        searchText: '',
        data : [],
        selectWhose : null,
        selectStartDate : null,


        filteredData : [],
        filteredWhose : [],
        visible: false,
        leave : [],
        employees : [],
        departments : [] 
    };
    
    reload () {
      this.setState({
        data : []
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
        console.log(e.response.data.error);
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
              filteredData : (res.data.list).reverse()
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
     
      axios.get('department/all', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            
            if (res.data.success === true) {
              this.setState({
                departments : res.data.list
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
      this.setState({
        visible: false,
      });
    };

    handleReset = () => {
      window.location.reload();
    //   this.props.form.resetFields();
    //   this.setState({
    //     selectWhose : 1,
    //     selectStartDate : 0,
    //     filteredData : this.state.data         
    // })
    }

    handleWhose = (value) => {
      console.log(value)

      if(value === 1) {
        axios.get('user/name', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            
            if (res.data.success === true) {
              console.log("||||||||||||", res.data.message)

              this.handleSelectedEmployee(res.data.message);
            } else {
                message.error(res.data.message);
            }
        })
        .catch(e => {
          message.error("Something went wrong");
          console.log(e.response.data.error);
        })

      }
      else{
        this.props.form.resetFields();
        this.setState({
            selectWhose : value,
            selectStartDate : 0,
            filteredData : this.state.data          
        })
      }  
    }

    handleSelectedEmployee = (value) => {
      console.log(value)

      this.props.form.resetFields();
      
      let arr = [];
      this.state.data.forEach(item => {
        if(item.user === value){
          arr.push(item);
        }
      })
      this.setState({ 
        filteredData : arr,
        filteredWhose : arr,
        selectStartDate : 0
       });
    }

    handleStartDate = (value) => {
        this.setState({
            selectStartDate : value,
            filteredData : this.state.filteredWhose           
        })
    }

    onChangeIs = (date, dateString) => {
        let arr = [];
        this.state.filteredWhose.forEach(item => {
            if(item.startDate === dateString){
                arr.push(item)
            }
        });
        this.setState({ filteredData : arr })
    }

    onChangeBetween = (date, dateString) => {
        var s = new Date(date[0])
        var e = new Date(date[1])

        let arr = [];
        this.state.filteredWhose.forEach(item => {
            var date = new Date(item.startDate)
            if(dateString[0] === item.startDate || dateString[1] === item.startDate){
                arr.push(item)
            }
            if(s-1 < date && date < e && !arr.includes(item)){
                arr.push(item)
            }
        });
        this.setState({ filteredData : arr })
    }

    onChangeAfter = (date, dateString) => {
        var s = new Date(date)

        let arr = [];
        this.state.filteredWhose.forEach(item => {
            var date = new Date(item.startDate)
            
            if( date > s ){
                arr.push(item)
            }

        });

        arr.forEach((item, index) => {
            if(dateString === item.startDate){
                if (index > -1) {
                    arr.splice(index, 1);
                }
            }
        });

        this.setState({ filteredData : arr })
    }

    onChangeBefore = (date, dateString) => {
        var s = new Date(date)

        let arr = [];
        this.state.filteredWhose.forEach(item => {
            var date = new Date(item.startDate)
            
            if( date < s ){
                arr.push(item)
            }
        });

        arr.forEach((item, index) => {
            if(dateString === item.startDate){
                if (index > -1) {
                    arr.splice(index, 1);
                }
            }
        });

        this.setState({ filteredData : arr })
    }

    handleSelectedDepartment = (value) => {
      this.props.form.resetFields();
      
      let arr = [];
      this.state.data.forEach(item => {
        if(item.department === value){
          arr.push(item);
        }
      })
      this.setState({ 
        filteredData : arr,
        filteredWhose : arr,
        selectStartDate : 0
       });
    }

    handleFilter = () => {

    }

    download = (args) => {
      var data, filename, link;

      var csv = convertArrayOfObjectsToCSV({
          data: this.state.filteredData
      });
      if (csv == null) return;

      filename = args.filename || 'export.csv';

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
        const { getFieldDecorator } = this.props.form;
       
        const {leave} = this.state;

        const columns = [
            {
              title: 'Name',
              dataIndex: 'user',
              ...this.getColumnSearchProps('user')
            },  
            {
                title: 'Date of request',
                dataIndex: 'requestDateTime'
            },
            {
              title: 'Leave Type',
              dataIndex: 'type',
              ...this.getColumnSearchProps('type')
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
              title: 'Checked By',
              dataIndex: 'checkBy'
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
                          <Tag color={color} key={tag}>
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
            }

          ];

        return (
            <div>
              <Card hoverable='true'>
                <Row>
                    <Col span={1}/>
                    <Col span={5}>
                        Whose: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <Select style={{ width: 200 }} onChange={this.handleWhose} defaultValue={0}>
                            <Option value={0}>All Employees</Option>
                            <Option value={1}>My</Option>
                            <Option value={2}>Selected Department</Option>
                            <Option value={3}>Selected Employee</Option>
                        </Select>
                    </Col>
                    <Col span={6}>
                        &nbsp;
                        {this.state.selectWhose === 3?
                        <Select placeholder="Please select employee" style={{ width: '250px' }} onChange={this.handleSelectedEmployee}>
                            {this.state.employees.map(item => (
                                <Option key={item.firstName +" "+ item.secondName}>{item.firstName} {item.secondName}</Option>
                            ))}
                        </Select>
                        :null
                        }
                        
                        {this.state.selectWhose === 2?
                        <Select placeholder="Please select department" style={{ width: '250px' }} onChange={this.handleSelectedDepartment}>
                            {this.state.departments.map(item => (
                                <Option key={item.name}>{item.name}</Option>
                            ))}
                        </Select>
                        :null
                        }
                    </Col>
                    <Col span={8}>
                        
                        <Button  type="primary" icon="search"  onClick={this.handleFilter} style={{ width: '120px' }}> Search </Button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <Button  type="primary" icon="undo"  onClick={this.handleReset} style={{ width: '120px' }}> Reset </Button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <Button  type="primary" icon="download"  onClick={this.download.bind(this,{ filename: "Leave History.csv" })}> Download </Button>
                    </Col>
                </Row> 
                <br/>
                <Row gutter={8}>
                    <Col span={1}/>
                    <Col span={5}>
                        Start Date: &nbsp;&nbsp;&nbsp;&nbsp;
                        {getFieldDecorator('start', {
                          initialValue : 0
                        })(
                        <Select style={{ width: 200 }} onChange={this.handleStartDate}>
                            <Option value={0}>All</Option>
                            <Option value={1}>Is</Option>
                            <Option value={2}>Between</Option>
                            <Option value={3}>After</Option>
                            <Option value={4}>Before</Option>
                        </Select>
                        )}
                    </Col>
                    <Col span={13}>
                        {this.state.selectStartDate === 1?
                        <DatePicker onChange={this.onChangeIs} style={{ width: 210 }}/>
                        :null
                        }
                        
                        {this.state.selectStartDate === 2?
                        <RangePicker onChange={this.onChangeBetween} style={{ width: 300 }}/>
                        :null
                        }

                        {this.state.selectStartDate === 3?
                        <DatePicker onChange={this.onChangeAfter} style={{ width: 210 }}/>
                        :null
                        }

                        {this.state.selectStartDate === 4?
                        <DatePicker onChange={this.onChangeBefore} style={{ width: 210 }}/>
                        :null
                        }
                    </Col>
                </Row>     
              </Card>   
              <br/>
              <Card hoverable='true'>
                <Table rowKey={record => record.id} columns={columns} dataSource={this.state.filteredData}  pagination={{ pageSize: 8 }} size="middle" />
              </Card> 

                {leave.length!==0? <Modal
                  title="Full Leave Record"
                  visible={this.state.visible}
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

const WrappedHistory = Form.create({ name: 'history' })(History);

export default WrappedHistory; 