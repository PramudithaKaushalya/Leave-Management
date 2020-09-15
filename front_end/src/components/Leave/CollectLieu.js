
import React from 'react';
import 'antd/dist/antd.css';
import axios from '../../config/axios'
import moment from 'moment';
import {
  Form,
  Input,
  Button,
  DatePicker,
  Card,
  Icon,
  message,
  Row,
  Col,
  Table,
  Modal,
  Radio,
  Tag
} from 'antd';

const { TextArea } = Input;
const { confirm } = Modal; 

class CollectLieu extends React.Component {

    state = {
        lieuLeaves : [],
        saveDates : [],
        holidays : []
    };
  
  componentWillMount() {

    this.getLieuLeaves();
    this.getHolidays();
    this.getHolidayEvents();
  }

  getLieuLeaves() {

    axios.get('lieu_leave/get_own', 
      {
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem("header")
          }
      })
      .then(res => {
         
        if (res.data.success) {  
          this.setState({
            lieuLeaves : res.data.list
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

  getHolidayEvents() {
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

  getHolidays() {
    axios.get('calender/dates', 
    {
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem("header")
        }
    })
    .then(res => {
      if (res.data.success === true) {  
        this.setState({
          holidays : res.data.list
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

  handleSubmit = e => {
    
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
    
        if (!err) {
            confirm({
            title: 'Sure to submit lieu collecting request?',
            content: 'If you submit, your request will be sent to administraters and supervisors.',
            okText: 'Submit',
            okType: 'primary',
              onOk: () => {
                if(!this.state.holidays.includes(moment(values.date).format("YYYY-MM-DD")) && values.date._d.getDay() !== 0 && values.date._d.getDay() !== 6){
                  message.error("Not a holiday or weekend"); 
                } else {
                  this.saveRequest(values);  
                }
              }
            })
        }
    })
  }

  saveRequest = (values) => {
    const leave = {
      date : values.date,
      period : values.period,
      project : values.project,
      worksDone : values.worksDone
    }

    axios.post(
        'lieu_leave/save', 
        leave, 
        { 
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        }
    )
    .then(res => {
        if (res.data.success) {              
            message.success(res.data.message); 
            this.getLieuLeaves();
            this.handleCancel();
        } else {
            message.error(res.data.message);
        }
    })
    .catch(e => {
        console.log(e.response.data.error);
        message.error("Something went wrong"); 
    })
  };

  handleCancel = () => {
    this.props.form.resetFields();
  };

  disabledStartDate = startValue => {
    // return startValue && startValue < moment().endOf('day');
    return startValue && startValue < moment().subtract(7,'d');
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    const { lieuLeaves, saveDates } = this.state;

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
        title: 'Date',
        key: '0',
        dataIndex: 'date',
        render: date => {
          
          return (
            moment(date).format('YYYY-MM-DD')
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
        title: 'Status',
        key: '3',
        dataIndex: 'status',
        render: tag => {
            let color;
            let text;
            if (tag === 0) {
                color = 'geekblue';
                text = 'Pending';
            } else if (tag === 1) {
              color = 'green';
              text = 'Approved';
            } else {
              color = 'red';
              text = 'Rejected';
            }
            return (
                <Tag color={color} key={tag}>
                    {text}
                </Tag>
            );
        }
      }
    ];

    const columnsForHolidays = [
      {
        title: 'Date',
        key: '0',
        dataIndex: 'date'
      },
      {
        title: 'Event',
        key: '1',
        dataIndex: 'reason',
      },    
    ];

    return (
        <div>
          <Row gutter={16}>
            <Col span={12} > 
              <Card type="inner" title='Collect Lieu Leaves' bordered={false} hoverable='true'>  
                <small>You can collect lieu leaves only for holidays or weekends.</small> <br/>
                <small>You have to collect lieu leaves one by one per day. </small> <br/>
                <small>You have to collect lieu leaves within a week. </small> <br/><br/>
                <br/>
                <Form {...formItemLayout} onSubmit={this.handleSubmit}>

                <Form.Item label="Date">
                {getFieldDecorator('date', {
                    rules: [{ required: true, message: 'Please input Date!' }],
                  })(
                  <DatePicker
                    disabledDate={this.disabledStartDate}
                    format="YYYY-MM-DD"
                    placeholder="Pickup a Date"
                    style={{ width: '100%' }}
                  />
                  )}
                </Form.Item>

                <Form.Item label="Period">
                {getFieldDecorator('period', {
                    rules: [{ required: true, message: 'Please input worked period!' }],
                    initialValue: 0
                  })(
                    <Radio.Group >
                        <Radio.Button value={0}>Half Day</Radio.Button>
                        <Radio.Button value={1}>Full Day</Radio.Button>
                    </Radio.Group>
                  )}
                </Form.Item>                

                <Form.Item label="Project">
                  {getFieldDecorator('project', {
                    rules : [{ 
                      required: true, 
                      message: 'Please input the project!' 
                    }],
                  })(<Input maxLength = {30}/>)}
                </Form.Item>
               
                <Form.Item label="Works done">
                  {getFieldDecorator('worksDone', {
                    rules: [{
                        required: true, 
                        message: 'Please input the works you done',
                      }],
                  })(<TextArea rows={4} maxLength = {300} style={{ width: '100%', height: '170px' }}/>)}
                </Form.Item>
                <br/>
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
                  <br/>
              </Card>
            </Col>
            <Col span={12}>
              <Row>
                <Col span={24}>
                  <Card hoverable='true'>
                  <h4>Requested Lieu Leaves</h4>
                  <Table rowKey={record => record.date} columns={columns} dataSource={lieuLeaves} size="small" pagination={{pageSize: 2}}/>
                  </Card>
                  <br/>
                </Col>
                <Col span={24}>
                  <Card hoverable='true'>
                  <h4>Holidays</h4>
                  <Table rowKey={record => record.date} columns={columnsForHolidays} dataSource={saveDates} size="small" pagination={{pageSize: 6}}/>
                  </Card>
                </Col>
              </Row>
            </Col>
          </Row>
        
    </div>
    );
  }
}

const WrappedCollectLieu = Form.create({ name: 'register' })(CollectLieu);

export default WrappedCollectLieu;         