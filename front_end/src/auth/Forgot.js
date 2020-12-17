import React from "react";
import { Link } from 'react-router-dom';
import { Icon, Form, Button, Input, Card, message } from 'antd';
import axios from '../config/axios'

class Forgot extends React.Component {

    state = {
        confirmDirty: false,
        button: "Send",
        email: null
    }

    changePassword = e => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
            const passwords = {
                email: this.state.email,  
                currentPassword : values.code,
                newPassword : values.newPassword
            }

            if (!err) {
                axios.post('api/auth/forgot_password', passwords)
                .then(res => {
                    if(res.data.success === true){
                    message.success(res.data.message);
                    this.props.form.resetFields(); 
                    }  else{
                    message.error(res.data.message); 
                    }
                }).catch(e => {
                console.log(e);
                message.error("Something went wrong"); 
                })
                
            }
        });
    }
    
    setEmail = (value) => {
        this.setState({email: value.target.value});
    }

    sendCode = () => {
        if(this.state.email!=null) {
            this.setState({button: "Sending..."});
            const user = {
                usernameOrEmail : this.state.email
            }
            axios.post('api/auth/forgot', 
                user)
                .then(res => {
                    if(res.data.success){
                        message.success(res.data.message);
                    } else {
                        message.error(res.data.message); 
                    } 
                    this.setState({button: "Send Again"});
            }).catch(e => {
                message.error("Something went wrong"); 
                this.setState({button: "Send Again"});
            })
        }
    }

    handleConfirmBlur = e => {
        const { value } = e.target;
        this.setState({ confirmDirty: this.state.confirmDirty || !!value });
    };
    
    compareToFirstPassword = (rule, value, callback) => {
        const { form } = this.props;
        if (value && value !== form.getFieldValue("newPassword")) {
          callback("The two passwords you entered is inconsistent!");
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

    render () {

        const { getFieldDecorator } = this.props.form;
       
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 16 },
              },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };

        return(
            <div className="container">
                <center>
                    <Card title="Forgot Password" bordered={false} style={{ width: '500px', height: '450px'}}>
                    
                        <Form layout="inline">
                            <Form.Item >
                                <Input 
                                    prefix={<Icon type="user" style={{ color: "rgba(0,0,0,.25)"}} />}
                                    placeholder="Your Employee Number"
                                    style={{ width: "300px" }}
                                    onChange={this.setEmail}
                                    required
                                    />
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit" onClick={this.sendCode} style={{width:'120px'}}>
                                        {this.state.button} 
                                </Button>
                            </Form.Item>
                        </Form>
                    
                    <br/> 
                    <br/>
                        <Form {...formItemLayout} layout="vertical" style={{width:'650px'}} hideRequiredMark>
                            <Form.Item> 
                                {getFieldDecorator('code', {
                                    rules: [{ 
                                    required: true, 
                                    len : 4,
                                    message: 'Please input valid number!', 
                                    }],
                                })(<Input  
                                    id= '1'
                                    prefix={<Icon type="key" style={{ color: "rgba(0,0,0,.25)" }} />}
                                    placeholder="Confirm Code"
                                    />)} 
                            </Form.Item>
                            
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
                                />)}
                            </Form.Item>
                            <Form.Item >
            
                            <Button onClick={this.changePassword} type="primary" htmlType="submit" className="login-form-button">
                            Reset Password
                            </Button>
                            <Link className="login-form-forgot" to='/login'>Log in</Link>
                            <br/>
                            </Form.Item>
                        </Form>
                    </Card>
                </center>                  
            </div>
        );
    }
}

const WrappedForgotPassword= Form.create({ name: 'register' })(Forgot);

export default WrappedForgotPassword;