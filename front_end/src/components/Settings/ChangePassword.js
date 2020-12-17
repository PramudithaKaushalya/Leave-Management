import React from "react";
import { Icon, Form, Button, Input, Card, message } from 'antd';
import axios from '../../config/axios';

class ChangePassword extends React.Component {

    state = {
        confirmDirty: false
    }

    changePassword = e => {
        e.preventDefault();
        this.props.form.validateFieldsAndScroll((err, values) => {
          const passwords = {
            currentPassword : values.currentPassword,
            newPassword : values.newPassword
          }

          if (!err) {
            axios.post('api/auth/change_password', 
            passwords,
            {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem("header")
                }
            })
            .then(res => {
                if(res.data.success === true){
                  message.success(res.data.message);
                  this.props.form.resetFields();
                }  else{
                  message.error(res.data.message); 
                }
            }).catch(e => {
              console.log(e);
              message.error("Something went wrong!"); 
            })
            
          }
        });
    }
    
    handelCancel = () => {
      this.props.form.resetFields();
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

        return(
            <div>
                <Card type="inner" title='Change Login Password' bordered={false} hoverable='true'> 
                 <Form {...formItemLayout} layout="vertical" hideRequiredMark>
            
                    <Form.Item hasFeedback>
                        {getFieldDecorator("currentPassword", {
                        rules: [
                            {
                            required: true,
                            message: "Please input your current password!"
                            },
                            {
                            validator: this.validateToNextPassword
                            }
                        ]
                        })(<Input.Password 
                        id= '1'
                        prefix={<Icon type="lock" style={{ color: "rgba(0,0,0,.25)" }} />}
                        placeholder="Current Password"
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
                    <Form.Item {...tailFormItemLayout}>
                        <Button type="primary" onClick={this.changePassword} style={{width:'100px'}}>
                        <Icon type="check-circle" /> 
                            Submit
                        </Button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <Button type="danger" onClick={this.handelCancel} style={{width:'100px'}}>
                        <Icon type="close-circle" /> 
                            Cancel
                        </Button>
                    </Form.Item>
                </Form>
                </Card>
            </div>
        );
    }
}

const WrappedChangePassword= Form.create({ name: 'register' })(ChangePassword);

export default WrappedChangePassword;