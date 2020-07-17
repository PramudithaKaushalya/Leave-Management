
import React from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import './index.css';
// import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import {
  Form,
  Input,
  Tooltip,
  Icon,
  Select,
  Button,
  DatePicker,
  Radio,
  Card,
  Row,
  Col,
  InputNumber, 
  message,
  Upload
} from 'antd';

const { Option } = Select;

function getBase64(img, callback) {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result));
  reader.readAsDataURL(img);
}

function beforeUpload(file) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('You can only upload JPG/PNG file!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('Image must smaller than 2MB!');
  }
  return isJpgOrPng && isLt2M;
}


class AddEmployee extends React.Component {
  
  componentWillMount(){
    this.setState({
        supervisors : [],
        departments : [],
        roles : []
    })

    axios.get('user/supervisor', 
    {
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem("header")
        }
    })
    .then(res => {

        if (res.data.success === true) {  
          this.setState({
              supervisors : res.data.list
          })
        } else {
          message.error(res.data.message);
        }
    })
    .catch(e => {
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

    axios.get('role/all', 
    {
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem("header")
        }
    })
    .then(res => {
        if (res.data.success === true) {  
          this.setState({
              roles : res.data.list
          })
        } else {
          message.error(res.data.message);
        }
    }) 
    .catch(e => {
      console.log(e.response.data.error);
    })
  } 

  state = {
    supervisors: [],
    departments: [],
    roles: [],
    confirmDirty: false,
    join_date: undefined,
    confirm_date: undefined,
    loading: false,
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        const employee = {
          userId : values.emp_id, 
          firstName: values.first_name || undefined,
          secondName: values.second_name || undefined,
          initials: values.initials || undefined,
          gender: values.gender || undefined,
          email: values.email || undefined,
          residence: values.residence || undefined,
          contact: values.contact || undefined,
          role: parseInt(values.role) || undefined,
          department: parseInt(values.department) || undefined,
          designation: values.designation,
          supervisor1: values.supervisor1 || undefined,
          supervisor2: values.supervisor2 || undefined,
          joinDate: this.state.join_date || undefined,
          confirmDate: this.state.confirm_date || "Not confirm yet",
          annual : values.annual,
          casual : values.casual,
          medical : values.medical,
          image : this.state.imageUrl
        }
        console.log('Values of object: ', this.state.imageUrl);

        axios.post(
          'api/auth/signup', 
          employee, 
          { 
              headers: {
                  Authorization: 'Bearer ' + localStorage.getItem("header")
              }
          }
        )
        .then(res => {
          if(res.data.success === true){
            console.log("res", res.data);
            message.success(res.data.message);
            this.props.form.resetFields(); 
            this.props.close();
          } else {
            message.error(res.data.message); 
          } 
        }).catch(e => {
          message.error("Something Went Wrong!"); 
        })
      }
    });
  };

  handleConfirmBlur = e => {
    const { value } = e.target;
    this.setState({ confirmDirty: this.state.confirmDirty || !!value });
  };

  compareToFirstPassword = (value, callback) => {
    const { form } = this.props;
    if (value && value !== form.getFieldValue('password')) {
      callback('Two passwords that you enter is inconsistent!');
    } else {
      callback();
    }
  };

  validateToNextPassword = (value, callback) => {
    const { form } = this.props;
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], { force: true });
    }
    callback();
  };

  onChangeJoin = (date, dateString) => {
    console.log(dateString);
    this.setState({ join_date : dateString });
  };

  onChangeConfirm = (date, dateString) => {
    console.log(dateString);
    this.setState({ confirm_date : dateString });
  };

  handleCancel = () => {
    this.props.form.resetFields(); 
    this.props.close();
  }

  handleChange = info => {
    if (info.file.status === 'uploading') {
      this.setState({ loading: true });
      return;
    }
    if (info.file.status === 'done') {
      // Get this url from response in real world.
      getBase64(info.file.originFileObj, imageUrl =>
        this.setState({
          imageUrl,
          loading: false,
        }),
      );
    }
  };

  render() {
    const { getFieldDecorator } = this.props.form;

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

    const uploadButton = (
      <div>
        {this.state.loading ? <Icon type="loading" /> : <Icon type="plus" />}
        <div className="ant-upload-text">Upload</div>
      </div>
    );

    const { imageUrl } = this.state;

    return (
        <div>
        <Card bordered={false} style={{ height: '1000px'}}>  
        <Form {...formItemLayout}>        
   
        <Form.Item label="Employee ID">
          {getFieldDecorator('emp_id', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input employee id!' }],
          })(<Input />)}
        </Form.Item>
        <Form.Item label="First Name">
          {getFieldDecorator('first_name', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input first name!' }],
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Second Name">
          {getFieldDecorator('second_name', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input second name!' }],
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Initials">
          {getFieldDecorator('initials', {
            rules: [{ transform: (value) => value.trim() },{ message: 'Please input initials!' }],
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Gender">
          {getFieldDecorator('gender')(
          <Radio.Group name="radiogroup">
          <Radio value="Male">Male</Radio>
          <Radio value="Female">Female</Radio>
          </Radio.Group>
        )}
        </Form.Item>       
        <Form.Item label="E-mail">
          {getFieldDecorator('email', {
            rules: [
              { transform: (value) => value.trim() },
              {
                type: 'email',
                message: 'The input is not valid E-mail!',
              },
              {
                required: true,
                message: 'Please input your E-mail!',
              },
            ],
          })(<Input />)}
        </Form.Item>
        <Form.Item
          label={
            <span>
              Residence Address&nbsp;
              <Tooltip title="No, Street, City.">
                <Icon type="question-circle-o" />
              </Tooltip>
            </span>
          }
        >
          {getFieldDecorator('residence', {
            rules: [{ message: 'Please input your nickname!', whitespace: true }],
          })(<Input />)}
        </Form.Item>
       
        <Form.Item label="Contact Number">
          {getFieldDecorator('contact', {
            rules: [{ transform: (value) => value.trim() },
              { 
              required: true, 
              len : 10,
              message: 'Please input valid number!', 
            }],
          })(<Input style={{ width: '100%' }} placeholder="ex: 0712345678"/>)} 
        </Form.Item>
        
        <Form.Item label="Role">
          {getFieldDecorator('role', {
            rules: [{ required: true, message: 'Please input employee role!' }],
          })(
            <Select placeholder="Please select a role">
               {this.state.roles.map(item => (
                  <Option key={item.id}>{item.name}</Option>
                ))}
            </Select>
          )}
        </Form.Item>  
        <Form.Item label="Department">
          {getFieldDecorator('department', {
            rules: [{ required: true, message: 'Please input employee department!' }],
          })(
          <Select placeholder="Please select a department">
            {this.state.departments.map(item => (
                  <Option key={item.id}>{item.name}</Option>
            ))}
          </Select>
            )}
        </Form.Item>  
        <Form.Item label="Designation">
          {getFieldDecorator('designation', {
            rules: [{ required: true, message: 'Please input designation!' }],
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Supervisor 01">
          {getFieldDecorator('supervisor1', {
            rules: [{ required: false, message: 'Please input first supervisor!' }],
          })(
            <Select placeholder="Please select a supervisor">
               {this.state.supervisors.map(item => (
                  <Option key={item.firstName}>{item.firstName} {item.secondName}</Option>
                ))}
            </Select>  
          )}
        </Form.Item>
        <Form.Item label="Supervisor 02">
          {getFieldDecorator('supervisor2', {
            rules: [{ required: false, message: 'Please input second supervisor!' }],
          })(
            <Select placeholder="Please select a supervisor">
            {this.state.supervisors.map(item => (
               <Option key={item.firstName}>{item.firstName} {item.secondName}</Option>
             ))}
            </Select> 
          )}
        </Form.Item>
        <Form.Item label="Joined date">
        {getFieldDecorator('join', {
            rules: [{ required: true, message: 'Please input employee joining date!' }],
          })(
          <DatePicker onChange={this.onChangeJoin} format="YYYY-MM-DD" />
          )}
        </Form.Item>
        <Form.Item label="Confirm date">
          <DatePicker onChange={this.onChangeConfirm} format="YYYY-MM-DD" />
        </Form.Item> 
        <Form.Item label="Annual Count">
          {getFieldDecorator('annual', {
            rules: [{ required: true, message: 'Please input annual leave count!' }],
          })(<InputNumber  />)}
        </Form.Item>
        <Form.Item label="Casual Count">
          {getFieldDecorator('casual', {
            rules: [{ required: true, message: 'Please input casual leave count!' }],
          })(<InputNumber  />)}
        </Form.Item>
        <Form.Item label="Medical Count">
          {getFieldDecorator('medical', {
            rules: [{ required: true, message: 'Please input medical leave count!' }],
          })(<InputNumber  />)}
        </Form.Item>

        <Form.Item label="Employee Image">
          {getFieldDecorator('image')(
            <Upload
              name="avatar"
              listType="picture-card"
              className="avatar-uploader"
              showUploadList={false}
              action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
              beforeUpload={beforeUpload}
              onChange={this.handleChange}
            >
              {imageUrl ? <img src={imageUrl} alt="avatar" style={{ width: '100%' }} /> : uploadButton}
            </Upload>
          )}
        </Form.Item>

        <br/><br/>  
        <Row>
        <Col span={17}>  
        <Form.Item {...tailFormItemLayout}>
          <Button type="primary" htmlType="submit" onClick={this.handleSubmit} style={{width:'100px', float:'right'}}>
          <Icon type="check-circle" /> 
            Submit
          </Button>
        </Form.Item>
        </Col> 
        <Col span={7}> 
        <Form.Item {...tailFormItemLayout}>
          <Button type="danger" onClick={this.handleCancel} style={{width:'100px', float:'left'}}>
          <Icon type="close-circle" /> 
            Cancel
          </Button>
        </Form.Item>
        </Col> 
        </Row>
      </Form>
      </Card>
      </div>
    );
  }
}

const WrappedAddEmployee = Form.create({ name: 'register' })(AddEmployee);

export default WrappedAddEmployee;
          
/*  */