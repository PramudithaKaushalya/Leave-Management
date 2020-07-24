
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
        lieuLeaves : []
    };
  
  componentWillMount() {

    this.getLieuLeaves();
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

    handleSubmit = e => {
      
      e.preventDefault();
      this.props.form.validateFieldsAndScroll((err, values) => {
      
          if (!err) {
              confirm({
              title: 'Sure to submit lieu collecting request?',
              content: 'If you submit, Send request to administraters and supervisors.',
              okText: 'Sumbit',
              okType: 'primary',
                  onOk: () => {
                          
                      const leave = {
                          date : values.date,
                          period : values.period,
                          project : values.project,
                          worksDone : values.worksDone
                      }

                      console.log("Lieu leave request --------------> ", leave);

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
                  }
              })
          }
      })
    }

  handleCancel = () => {
    this.props.form.resetFields();
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    const { lieuLeaves } = this.state;

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
        title: 'Status',
        key: '3',
        dataIndex: 'isApproved',
        render: tag => {
            let color;
            let text;
            if (tag) {
                color = 'green';
                text = 'Approved';
            }else {
                color = 'geekblue';
                text = 'Pending';
            }
            return (
                <Tag color={color} key={tag}>
                    {text}
                </Tag>
            );
        }
      }
    ];

    return (
        <div>
          <Row gutter={16}>
            <Col span={13} > 
              <Card type="inner" title='Collect Lieu Leaves' bordered={false} hoverable='true'>  
                <p>You have to collect lieu leaves day by day.</p>
                <Form {...formItemLayout} onSubmit={this.handleSubmit}>

                <Form.Item label="Date">
                {getFieldDecorator('date', {
                    rules: [{ required: true, message: 'Please input Date!' }],
                  })(
                  <DatePicker
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
            <Col span={11}>
              <Card hoverable='true'>
              <Table rowKey={record => record.date} columns={columns} dataSource={lieuLeaves} size="small" pagination={false}/>
              </Card>
            </Col>
          </Row>
        
    </div>
    );
  }
}

const WrappedCollectLieu = Form.create({ name: 'register' })(CollectLieu);

export default WrappedCollectLieu;         