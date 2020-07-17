
import React from 'react';
import axios from '../../config/axios'
import moment from 'moment';
import 'antd/dist/antd.css';
import './index.css';
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
  message,
  InputNumber,
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

class UpdateEmployee extends React.Component {

  componentDidUpdate() {
    console.log("------------------------------> ",this.props.update.id, " *** ", this.state.data.id)

    if(this.state.data.id !== this.props.update.id) {
      this.setState({
        data : this.props.update,
        imageUrl : this.props.update.image
      })
    }
  }

  componentWillMount(){
  
    console.log("+++++++++++++++++++++++++++++++",this.props.empId)
    this.setState({
      // data : this.props.update,
      role : this.props.update.role,
      department : this.props.update.department,
      supervisors : [],
      departments : [],
      roles : [],
      // imageUrl : this.props.update.image
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
      message.error("Something went wrong");
      console.log(e.response.data.error);
    }) 
    
  }

  state = {
    empId: null,
    data: [],
    supervisors : [],
    departments : [],
    roles : [],
    autoCompleteResult: [],
    join_date: undefined,
    confirm_date: undefined,
    role: undefined,
    department: undefined,
    joinFromRes: "2019-01-01",
    confirmFromRes: "2019-12-31",
    imageUrl: null
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        const employee = {
          userId: values.emp_id,
          firstName: values.first_name,
          secondName: values.second_name,
          initials: values.initials,
          gender: values.gender,
          email: values.email,
          residence: values.residence,
          contact: values.contact,
          role: values.role || this.state.role,
          department: values.department || this.state.department,
          designation: values.designation,
          supervisor1: values.supervisor1,
          supervisor2: values.supervisor2,
          joinDate: this.state.join_date || this.state.data.joinDate,
          confirmDate: this.state.confirm_date || this.state.data.confirmDate,
          annual : values.annual,
          casual : values.casual,
          medical : values.medical,
          image : this.state.imageUrl 
        }

        axios.post('user/update/'+this.state.data.id, employee, 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {

          if (res.data.success === true) {  
            message.success(res.data.message);
            this.handleCancel();
          } else {
            message.error(res.data.message);
          }
        })
        .catch(e => {
          message.error("Unsuccessfull");
          console.log("res", e)
        })
      
      
      }
    });
  };

  handleCancel = () => {
    this.props.close();
    // this.props.form.resetFields();
  //   this.setState({
  //     data : []
  // })
  }

  onChangeJoin = (date, dateString) => {
    console.log("Date String: ",dateString);
    console.log("Date: ", date);
    this.setState({ join_date : dateString });
  };

  onChangeConfirm = (date, dateString) => {
    console.log(dateString);
    this.setState({ confirm_date : dateString });
  };

  handleChange = info => {
    if (info.file.status === 'uploading') {
      this.setState({ loading: true });
      return;
    }
    if (info.file.status === 'done') {
      // Get this url from response in real world.
      getBase64(info.file.originFileObj, imageUrl =>
        this.setState({
          imageUrl: imageUrl,
          loading: false,
        }),
      );
    }
  };

  render() {
    const { getFieldDecorator } = this.props.form;
    const { update } = this.props;

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
        <div key={this.props.empId}>
        <Card bordered={false} style={{ height: '1000px'}}>  
        <Form {...formItemLayout} onSubmit={this.handleSubmit}>
        
        <Form.Item label="Employee ID">
          {getFieldDecorator('emp_id', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input employee id!' }],
            initialValue: this.state.data.userId
          })(<Input />)}
        </Form.Item>
        <Form.Item label="First Name">
          {getFieldDecorator('first_name', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input first name!' }],
            initialValue: this.state.data.firstName
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Second Name">
          {getFieldDecorator('second_name', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input second name!' }],
            initialValue: this.state.data.secondName
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Initials">
          {getFieldDecorator('initials', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input initials!' }],
            initialValue: this.state.data.initials
          })(<Input />)}
        </Form.Item>
        <Form.Item label="Gender">
          {getFieldDecorator('gender', {
            rules: [{ required: true, message: 'Please input initials!' }],
            initialValue: this.state.data.gender
          })(
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
            initialValue: this.state.data.email
          })(<Input />)}
        </Form.Item>
        <Form.Item
          label={
            <span>
              Residence Address&nbsp;
              <Tooltip title="No/ Street/ City.">
                <Icon type="question-circle-o" />
              </Tooltip>
            </span>
          }
        >
          {getFieldDecorator('residence', {
            rules: [{ message: 'Please input your nickname!', whitespace: true }],
            initialValue: this.state.data.residence
          })(<Input />)}
        </Form.Item>
       
        <Form.Item label="Contact Number">
          {getFieldDecorator('contact', {
            rules: [{ transform: (value) => value.trim() },
              { 
              required: true, 
              message: 'Please input valid number!',
              len : 10 
            }],
            initialValue: this.state.data.contact
          })(<Input style={{ width: '100%' }} />)}
        </Form.Item>

        <Form.Item label="Role">
          {getFieldDecorator('role', {
            rules: [{ required: true, message: 'Please input role!' }],
            initialValue: this.state.role
          })(
            <Select>
              {this.state.roles.map(item => (
                  <Option key={item.name}> {item.name}</Option>
              ))}
            </Select>
            )}
        </Form.Item>  

        <Form.Item label="Department">
        {getFieldDecorator('department', {
            rules: [{ required: true, message: 'Please input your department!' }],
            initialValue: this.state.department
          })(
          <Select>
            {this.state.departments.map(item => (
                  <Option key={item.name}>{item.name}</Option>
            ))}
          </Select>
          )}
        </Form.Item>  

        <Form.Item label="Designation">
          {getFieldDecorator('designation', {
            rules: [{ transform: (value) => value.trim() },{ required: true, message: 'Please input designation!' }],
            initialValue: this.state.data.designation
          })(<Input />)}
        </Form.Item>

        <Form.Item label="Supervisor 01">
          {getFieldDecorator('supervisor1', {
            rules: [{ required: false, message: 'Please input first supervisor!' }],
            initialValue: this.state.data.supervisor1
          })(
            <Select>
            {this.state.supervisors.map(item => (
                  <Option key={item.firstName}>{item.firstName} {item.secondName}</Option>
            ))}
          </Select>
          )}
        </Form.Item>

        <Form.Item label="Supervisor 02">
          {getFieldDecorator('supervisor2', {
            rules: [{ message: 'Please input second supervisor!' }],
            initialValue: this.state.data.supervisor2
          })(
            <Select>
            {this.state.supervisors.map(item => (
                  <Option key={item.firstName}>{item.firstName} {item.secondName}</Option>
            ))}
          </Select>
          )}
        </Form.Item>

        <Form.Item label="Joined date">
          <DatePicker onChange={this.onChangeJoin} format="YYYY-MM-DD" defaultValue={moment(update.joinDate, "YYYY-MM-DD")}/>
        </Form.Item>

        <Form.Item label="Confirm date">
          <DatePicker onChange={this.onChangeConfirm} format="YYYY-MM-DD" defaultValue={moment(update.confirmDate, "YYYY-MM-DD")}/>
        </Form.Item> 
        
        <Form.Item label="Annual Count">
          {getFieldDecorator('annual', {
            rules: [{ required: true, message: 'Please input annual leave count!' }],
            initialValue: this.state.data.annual 
          })(<InputNumber />)}
        </Form.Item>

        <Form.Item label="Casual Count">
          {getFieldDecorator('casual', {
            rules: [{ required: true, message: 'Please input casual leave count!' }],
            initialValue: this.state.data.casual
          })(<InputNumber />)}
        </Form.Item>

        <Form.Item label="Medical Count">
          {getFieldDecorator('medical', {
            rules: [{ required: true, message: 'Please input medical leave count!' }],
            initialValue: this.state.data.medical
          })(<InputNumber />)}
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
          <Button type="primary" htmlType="submit" style={{width:'100px', float:'right'}}>
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

const WrappedAddEmployee = Form.create({ name: 'register' })(UpdateEmployee);

export default WrappedAddEmployee;