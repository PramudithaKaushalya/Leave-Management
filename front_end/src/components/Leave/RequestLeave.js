
import React from 'react';
import 'antd/dist/antd.css';
import axios from '../../config/axios'
import moment from 'moment';
import {
  Form,
  Input,
  Button,
  DatePicker,
  Radio,
  Switch,
  Card,
  Icon,
  Select,
  message,
  Row,
  Col,
  Table,
  Modal
} from 'antd';

const { TextArea } = Input;
const { Option } = Select;
const { confirm } = Modal; 

class RequestLeave extends React.Component {
  
  componentWillMount() {
    this.setState({
      employees : [],
      types : [],
      summery : []
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
              summery : res.data.list
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

      axios.get('leave_type/all', 
      {
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem("header")
          }
      })
      .then(res => {
         
        if (res.data.success === true) {  
          this.setState({
            types : res.data.list
          })
        } else {
          message.error(res.data.message);
        }
      })
      .catch(e => {
        message.error("Something went wrong");
        console.log(e.response.data.error);
      }) 

      axios.get('calender/all', 
      {
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem("header")
          }
      })
      .then(res => {
        if (res.data.success === true) {  
          this.setState({
            saveDates : res.data.list
          });
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
    types : [],
    employees : [],
    summery : [],
    period : null,
    disable : false,
    startValue: null,
    endValue: null,
    endOpen: false,
    type: null,
    error: null,
    duty: null,
    start_half: "Full Day",
    end_half: "Full Day",
    start_full_disable: false,
    start_morning_disable: true,
    half_day: false,
    start_day : null,
    end_day : null,
    saveDates : [],
    count: 0
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      
      if (!err && this.state.error==null) {
        confirm({
          title: 'Sure to submit your leave request?',
          content: 'If you submit, Send request to administraters and supervisors, will inform duty coverer.',
          okText: 'Sumbit',
          okType: 'primary',
          onOk: () => {
        
            if(this.state.duty !== null){ 
              const leave = {
              leave_type : { leave_type_id : this.state.type } || undefined,
              user : { id : this.parseJwt(localStorage.getItem("header")) } || undefined,
              startDate : this.state.startValue || undefined,
              end_date : this.state.endValue || this.state.startValue,
              startHalf : this.state.start_half,
              endHalf : this.state.end_half,
              number_of_leave_days : parseFloat(values.days) || undefined,
              duty : {id : parseInt(this.state.duty)}, 
              special_notes : values.note || "No special note.",
              status : "Pending",
              reject : "Not reject"
              }

              axios.post(
                'leave/request', 
                leave, 
                { 
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem("header")
                    }
                }
              )
              .then(res => {
                if (res.data.success === true) {              
                  message.success(res.data.message); 
                  this.handleCancel();
                } else {
                    message.error(res.data.message);
                }
              })
              .catch(e => {
                console.log(e.response.data.error);
                message.error("Something went wrong"); 
              })
            }else{
              const leave = {
                leave_type : { leave_type_id : this.state.type } || undefined,
                user : { id : this.parseJwt(localStorage.getItem("header")) } || undefined,
                startDate : this.state.startValue || undefined,
                end_date : this.state.endValue || this.state.startValue,
                startHalf : this.state.start_half,
                endHalf : this.state.end_half,
                number_of_leave_days : parseFloat(values.days) || undefined,
                special_notes : values.note || "No special note",
                status : "Pending",
                reject : "Not reject"
              }

              axios.post(
                'leave/request', 
                leave, 
                { 
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem("header")
                    }
                }
              )
              .then(res => {
                if (res.data.success === true) {              
                  message.success(res.data.message); 
                  this.handleCancel();
                } else {
                  message.error(res.data.message);
                }
              })
              .catch(e => {
                console.log(e.response.data.error);
                message.error("Something went wrong"); 
              })  
            }
          }
        }) 
      }else if(this.state.error !== null){
          message.error(this.state.error);
      }
    });
  }

  handleCancel = () => {
    this.props.form.resetFields();
    this.setState({
      disable : false,
      period : null,
      start_full_disable : false,
      start_morning_disable : true,
      start_half : "Full Day",
      end_half : "Full Day",
      error: null,
      count : 0
    });
  };

  disabledStartDate = startValue => {
    // return startValue && startValue < moment().endOf('day');
    return startValue && startValue < moment().subtract(7,'d');
  };

  disabledEndDate = endValue => {
    const { start_day } = this.state;
    if (!endValue || !start_day) {
      return false;
    }
    return endValue.valueOf() <= start_day.valueOf();
  };

  onChange = (field, value) => {
    this.setState({
      [field]: value,
    });
  };

  onStartChange = (date, dateString) => {
    this.onChange('startValue', dateString);
    this.onChange('start_day',date)
  };

  onEndChange = (date, dateString) => {

    var e = new Date(date)
    var s = new Date(this.state.startValue)
    var dif = parseInt(e-s)/(1000*60*60*24)

    this.state.saveDates.map(date => {
      var x = new Date(date.date)
      if(s<=x && x<=e) {
        this.onChange('count', (this.state.count+1))
      }
      return null;
    })

    this.onChange('endValue', dateString);
    this.onChange('end_day', date);
    this.onChange('start_half', "Full Day");
    this.onChange('end_half', "Full Day");

    setTimeout(() => {
      if(date !== null && this.state.start_day !== null){
        if(date._d.getDay() === 6){
          this.onChange('period', parseInt(dif)-this.state.count);
          this.checkCount(parseInt(dif)); 
        }else if(this.state.start_day._d.getDay() === 0){
          this.onChange('period', parseInt(dif)-this.state.count);
          this.checkCount(parseInt(dif)); 
        }else if(date._d.getDay() > this.state.start_day._d.getDay() && parseInt(dif)+1 < 6){
          this.onChange('period', parseInt(dif)+1-this.state.count);
          this.checkCount(parseInt(dif)+1); 
        }else if(date._d.getDay() < this.state.start_day._d.getDay() && parseInt(dif)+1 < 6 ){
          this.onChange('period', parseInt(dif)+1-2-this.state.count);
          this.checkCount(parseInt(dif)+1-2);
        }else if(date._d.getMonth() !== this.state.start_day._d.getMonth()){
          this.onChange('period', parseInt(dif)+1-2-this.state.count);
          this.checkCount(parseInt(dif)+1-2);
        }else{
          var x = (Math.floor((parseInt(dif)+1) / 7)*2)
          this.onChange('period', parseInt(dif)+1-x-this.state.count); 
          this.checkCount(parseInt(dif)+1-x);
        }
      }
      this.onChange('count', 0)
    }, 100);
  };

  setType = (e) => {
    e.preventDefault();
    this.handleCancel();
    this.onChange('type', this.state.types[e.target.value].leave_type_id);
  }

  checkCount = (period) => {
    if(period > 100 ){
      this.onChange('error', "* Select in range");
    }else if(this.state.type === 1 && period > (this.state.summery[0].remaining)){
      this.onChange('error', "* Exceeded your casual limit");
    }else if(this.state.type === 2 && period > (this.state.summery[1].remaining)){
      this.onChange('error', "* Exceeded your medical limit");
    }else if(this.state.type === 5 && period > (this.state.summery[2].remaining)){
      this.onChange('error', "* Exceeded your annual limit");  
    }else if(this.state.type === 6 && period > (this.state.summery[3].remaining)){
      this.onChange('error', "* Exceeded your lieu limit");  
    }else this.onChange('error', null);  
  }

  handleStartChange = open => {
    if (!open && !this.state.disable) {
      this.setState({ endOpen: true });
    }
  };

  handleEndChange = open => {
    this.setState({ endOpen: open });
  };

  halfDay = (value) => {
    this.onChange('half_day', value);
    if(value){
      this.setState({
        period : 0.5,
        disable : true,
        endValue : this.state.startValue,
        start_full_disable : true,
        start_morning_disable : false,
        start_half : "Morning"
      })
      this.checkCount(0.5);
    }else{
      this.setState({
        period : null,
        disable : false,
        error : null,
        start_full_disable : false,
        start_morning_disable : true,
        start_half : "Full Day"
      })
    }  
  } 

  handleDuty = (emp) => {
    this.setState({
      duty: emp
    })
  } 

  startHalfChange = (e) => {
    this.setState({
      start_half: e.target.value
    })
    if(e.target.value === "Evening" && !this.state.half_day && this.state.period!==null){
      this.onChange('period', this.state.period-0.5);
      this.checkCount(this.state.period-0.5);
    } else if(e.target.value === "Full Day" && this.state.period!==null){
      this.onChange('period', this.state.period+0.5);
      this.checkCount(this.state.period+0.5);
    }
    
  }

  endHalfChange = (e) => {
    this.setState({
      end_half: e.target.value
    })
    if(e.target.value === "Morning" && this.state.period!==null){
      this.onChange('period', this.state.period-0.5);
      this.checkCount(this.state.period-0.5);
    } else if(e.target.value === "Full Day" && this.state.period!==null){
      this.onChange('period', this.state.period+0.5);
      this.checkCount(this.state.period+0.5);
    }
  }

  render() {
    const { getFieldDecorator } = this.props.form;
    const { endOpen, summery, types } = this.state;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
    };
    
    const tailFormItemLayout = {
      wrapperCol: {
        xs: {
          span: 24,
          offset: 0,
        },
        sm: {
          span: 16,
          offset: 8,
        },
      },
    };

    const columns = [
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

    return (
        <div>
          <Row gutter={16}>
            <Col span={15} > 
              <Card type="inner" title='Leave Request Form' bordered={false} hoverable='true'>  
                <Form {...formItemLayout} onSubmit={this.handleSubmit}>

                <Form.Item label="Leave Type">
                  {getFieldDecorator('leave_type', {
                    rules: [{ required: true, message: 'Please input leave type!' }],
                  })(
                  <Radio.Group name="radiogroup" style={{ width: '450px' }} onChange={this.setType}>
                  {types.map((item , index) => (
                          <Radio.Button key={item.leave_type_id} value={index}>{item.type}</Radio.Button>
                  ))}   
                  </Radio.Group>
                )}
                </Form.Item>  
              
                <Form.Item label="Half Day">
                  {getFieldDecorator('halfDay', {
                    valuePropName: 'checked'
                  })(
                  <Switch
                    checkedChildren={<Icon type="check" />}
                    unCheckedChildren={<Icon type="close" />}
                    onChange= {this.halfDay} 
                  />
                  )}
                </Form.Item>

                <Form.Item label="Start date">
                {getFieldDecorator('start_date', {
                    rules: [{ required: true, message: 'Please input Start Date!' }],
                  })(
                  <DatePicker
                    disabledDate={this.disabledStartDate}
                    format="YYYY-MM-DD"
                    placeholder="Pickup a Date"
                    onChange={this.onStartChange}
                    onOpenChange={this.handleStartChange}
                    style={{ width: '280px' }}
                  />
                  )}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <Radio.Group onChange={this.startHalfChange} value={this.state.start_half}>
                    {this.state.start_full_disable ? null : <Radio value="Full Day" >Full Day</Radio>}
                    {this.state.start_morning_disable ? null :<Radio value="Morning" >Morning</Radio>}
                    <Radio value="Evening">Evening</Radio>
                  </Radio.Group>
                </Form.Item>

                {this.state.disable ? null :
                <Form.Item label="End date">
                  {getFieldDecorator('end_date')(
                  <DatePicker
                    disabledDate={this.disabledEndDate}
                    format="YYYY-MM-DD"
                    placeholder="Pickup a Date"
                    onChange={this.onEndChange}
                    open={endOpen}
                    onOpenChange={this.handleEndChange}
                    style={{ width: '280px' }}
                  />
                  )}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <Radio.Group onChange={this.endHalfChange} value={this.state.end_half}>
                    <Radio value="Full Day">Full Day</Radio>
                    <Radio value="Morning">Morning</Radio>
                  </Radio.Group>
                </Form.Item>
                }

                <Form.Item label="Number of leave days">
                  {getFieldDecorator('days', {
                    rules : [{ 
                      required: true, 
                      message: 'Please input End Date to get count!' 
                    }],
                    initialValue : this.state.period
                  })(<Input style={{width: '50px'}} disabled={true}/>)}
                  <span style={{color:'red'}}>&nbsp;&nbsp; {this.state.error}</span>
                </Form.Item>
                {this.state.employees.length!==0?  
                <Form.Item label="Duty Cover By">
                  {getFieldDecorator('duty', {
                    initialValue : 'No One'
                  })(
                    <Select placeholder="Please select a employee" style={{ width: '400px' }} onChange={this.handleDuty} >
                    {this.state.employees.map(item => (
                          <Option key={item.id}>{item.firstName} {item.secondName}</Option>
                    ))}
                  </Select> )}
                </Form.Item>
                : null }      
                <Form.Item label="Special Notes">
                  {getFieldDecorator('note', {
                    rules: [{
                        message: 'Please input your note',
                      }],
                  })(<TextArea maxLength='200' rows={4} style={{ width: '400px' }}/>)}
                </Form.Item>

                <Form.Item {...tailFormItemLayout}>
                  <Button type="primary" htmlType="submit" style={{width:'100px'}}>
                  <Icon type="check-circle" /> 
                    Submit
                  </Button>
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <Button type="danger" onClick={this.handleCancel} style={{width:'100px'}}>
                  <Icon type="close-circle" /> 
                    Cancel
                  </Button>
                </Form.Item>
              </Form>
              </Card>
            </Col>
            <Col span={9}>
              <Card hoverable='true'>
              <Table rowKey={record => record.type} columns={columns} dataSource={summery} size="small" pagination={false}/>
              </Card>
            </Col>
          </Row>
        
    </div>
    );
  }
}

const WrappedRequestLeave = Form.create({ name: 'register' })(RequestLeave);

export default WrappedRequestLeave;         