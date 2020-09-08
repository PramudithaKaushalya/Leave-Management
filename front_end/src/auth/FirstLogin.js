import React from 'react';
import axios from '../config/axios'
import 'antd/dist/antd.css';
import './index.css';
import { Form, Icon, Input, Button, Card, message } from 'antd';


class FirstLogin extends React.Component {
    state = {
        confirmDirty: false
    }

    
    componentWillMount () {
        if(localStorage.getItem("header") === null){
            this.props.history.push('/login');
        }
    }
    
    changePassword = e => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            const passwords = {
                email : localStorage.getItem("header"),
                newPassword : values.newPassword
            }

            if (!err) {
                axios.post('api/auth/first_login_password', passwords)
                .then(res => {
                    if(res.data.success === true){
                        message.success(res.data.message);
                        this.props.form.resetFields();
                        this.props.history.push('/login');
                    }  else{
                        message.error(res.data.message); 
                    }
                }).catch(e => {
                    console.log(e);
                    message.error("Something went wrong!"); 
                })
                
            }
        });
        localStorage.removeItem("header");
    }

    handleConfirmBlur = e => {
        const { value } = e.target;
        this.setState({ confirmDirty: this.state.confirmDirty || !!value });
    };

    compareToFirstPassword = (rule, value, callback) => {
        const { form } = this.props;
        if (value && value !== form.getFieldValue("newPassword")) {
        callback("Two passwords that you enter is inconsistent!");
        } else {
        callback();
        }
    };

    validateToNextPassword = (rule, value, callback) => {
        const { form } = this.props;
        if (value && this.state.confirmDirty) {
        form.validateFields(["confirm"], { force: true });
        }
        callback();
    };

    render() {
        
        const { getFieldDecorator } = this.props.form;
        
        const formItemLayout = {
        labelCol: {
            xs: { span: 24 },
            sm: { span: 24 },
        },
        wrapperCol: {
            xs: { span: 24 },
            sm: { span: 24 },
        },
        };

        const tailFormItemLayout = {
            wrapperCol: {
                xs: {
                span: 16,
                offset: 0,
                },
                sm: {
                span: 24,
                offset: 0,
                },
            },
        };

        return (
        <div className= "container">
            <center>
            <Card title="First Login" bordered={false} style={{ width: '400px', height: '350px', flex: 0.5}}>
                <Form  {...formItemLayout} className="login-form">
                <br/>
                <Form.Item hasFeedback>
                    {getFieldDecorator("newPassword", {
                        rules: [
                            {
                            required: true,
                            message: "Please input your new password!"
                            },
                            {
                            validator: this.validateToNextPassword
                            }
                        ]
                        })(<Input.Password 
                        id= '2'
                        prefix={<Icon type="lock" style={{ color: "rgba(0,0,0,.25)" }} />}
                        placeholder="New Password"
                        maxLength='20'
                        />)}
                </Form.Item>
                <Form.Item hasFeedback>
                    {getFieldDecorator("confirm", {
                    rules: [
                        {
                        required: true,
                        message: "Please confirm your new password!"
                        },
                        {
                        validator: this.compareToFirstPassword
                        }
                    ]
                    })(<Input.Password 
                    id= '3'
                    onBlur={this.handleConfirmBlur}
                    prefix={<Icon type="lock" style={{ color: "rgba(0,0,0,.25)" }} />}
                    placeholder="Confirm Password"
                    maxLength='20'
                    autocomplete="none"
                    display="none"
                    />)}
                </Form.Item>
              <br/><br/>
              <Form.Item {...tailFormItemLayout}>
                
                <Button onClick={this.changePassword} type="primary" htmlType="submit" className="login-form-button" >
                  <Icon type="check-circle" /> 
                  Change Password
                </Button>
                <br/>
              </Form.Item>
            </Form>
          </Card>
        </center>
      </div>
    );
  }
}

const WrappedFirstLogin = Form.create({ name: 'first_login' })(FirstLogin);

export default WrappedFirstLogin