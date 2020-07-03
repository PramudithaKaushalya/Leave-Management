import React from 'react';
import { Link } from 'react-router-dom';
import axios from '../config/axios'
import 'antd/dist/antd.css';
import './index.css';
import { Form, Icon, Input, Button, Card, message } from 'antd';

class NormalLoginForm extends React.Component {

  handleLogin = e => {
    e.preventDefault(); 
    this.props.form.validateFields((err, values) => {
      const user = {
        usernameOrEmail : values.username,
	      password : values.password
      }
      if (!err) {
        axios.post('api/auth/signin', user)
        .then(token => {

          if (token.data.success === true) {
            localStorage.setItem("header", token.data.accessToken);
            this.props.history.push('/');
          } else{
            message.error(token.data.message);
            this.props.form.resetFields(); 
          } 
        }).catch(e => {
          console.log(e);
          message.error(e);
        });
      }
    });
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
          <Card title="VX Leave Management System" bordered={false} style={{ width: '400px', height: '400px', flex: 0.5}}>
            <Form  {...formItemLayout} className="login-form">
              <br/>
              <Form.Item>
                {getFieldDecorator('username', {
                  rules: [{ required: true, message: 'Please input your username!' }],
                })(
                  <Input
                    style={{width: '300px'}}
                    prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                    placeholder="Username"
                  />,
                )}
              </Form.Item>
              <Form.Item>
                {getFieldDecorator('password', {
                  rules: [{ required: true, message: 'Please input your password!' }],
                })(
                  <Input.Password
                    style={{width: '300px'}}
                    prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                    type="password"
                    placeholder="Password"
                  />,
                )}
              </Form.Item>
              <br/><br/>
              <Form.Item {...tailFormItemLayout}>
                
                <Button onClick={this.handleLogin} type="primary" htmlType="submit" className="login-form-button" >
                  Log in
                </Button>
                <Link className="login-form-forgot" to='/forgot'>Forgot Password</Link>
                <br/>
              </Form.Item>
            </Form>
          </Card>
        </center>
      </div>
    );
  }
}

const WrappedNormalLoginForm = Form.create({ name: 'normal_login' })(NormalLoginForm);

export default WrappedNormalLoginForm