
import React from 'react';
import { Link, Switch, Route, Redirect } from 'react-router-dom';
import 'antd/dist/antd.css';
import './index.css';
import { Layout, Menu, Icon, Form, message, Spin } from 'antd';
import Dashboard from './Dashboard';
import LeavesHistory from '../components/Leave/ReadLeaves';
import RequestLeave from '../components/Leave/RequestLeave';
import PendingLeaves from '../components/Leave/PendingLeaves';
import ViewEmployee from '../components/Employee/ViewEmployee';
import ViewProfile from '../components/Leave/ViewProfile';
import History from '../components/Leave/History';
import ChangePassword from '../components/Settings/ChangePassword';
import LeaveCalender from '../components/Settings/LeaveCalender';
import ContactsNumber from '../components/Settings/ContactsNumber';
import Header from './Header';
import axios from '../config/axios';
import ViewOne from './../components/Employee/ViewOne';
import Attendence from './../components/Attendence/Attendence';
import OneAttendence from './../components/Attendence/OneAttendence';
import CollectLieu from '../components/Leave/CollectLieu';
import AddLieu from '../components/Leave/AddLieu';
import PendingLieu from '../components/Leave/PendingLieu';
import OwnPending from '../components/Leave/OwnPending';
import Error from './../components/Error';

const { Content, Footer, Sider } = Layout;
const { SubMenu } = Menu;

class SiderDemo extends React.Component {
  
  componentWillMount () {
    axios.get('api/auth/correct', 
      {
          headers: {
              Authorization: 'Bearer ' + localStorage.getItem("header")
          }
      })
      .then(res => {
          if(res.data.success === true){                      
            this.setState({
              redirectToReferrer: 1,
              spin: false
            }); 
          }else{
            this.setState({redirectToReferrer: 0});
            message.error(res.data.message); 
          }
      }).catch(e => {
          this.setState({redirectToReferrer: 0});
          // message.error("Something went wrong"); 
      })

    if(localStorage.getItem("header") !== null){ 

      axios.get('api/auth/info',
        {
          headers: {
            Authorization: 'Bearer ' + localStorage.getItem("header")
          }
        })
        .then(res => {
            if(res.data.success === true){
              this.setState({
                  user_name: res.data.employee.firstName+ " " + res.data.employee.secondName,
                  user_role: res.data.employee.role,
                  user_des: res.data.employee.designation,
                  user_department: res.data.employee.department,
                  user_image: res.data.employee.image,
                  spin: false 
              }) 
            }  else{
              message.error(res.data.message); 
            }
        }).catch(e => {
          console.log(e);
        });
    }

  }

  onCollapse = collapsed => {
    this.setState({ collapsed });
  };

  state = {
    redirectToReferrer: null,
    collapsed: false,
    confirmDirty: false,
    user_name: null,
    user_role: null,
    user_des: null,
    user_department: null,
    user_image: null,
    spin: true
  };

  render() {
    const {redirectToReferrer, user_name, user_role, user_des, user_department, user_image} = this.state;

    if (redirectToReferrer === 0) {
      return <Redirect to='/login'/>
    }else if (redirectToReferrer === 1){
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <Sider 
          style={{
            overflow: 'auto',
            height: '100vh',
            position: 'fixed',
            // left: 0,
          }}
          width = "235px"
          // collapsible
          // collapsed={this.state.collapsed}
          // onCollapse={this.onCollapse}
        >
          <div className="logo" />
          <br/>
          <br/>
          <Menu theme="dark" defaultSelectedKeys={"1"} mode="inline">
            
          { user_role === "Admin"?
            <Menu.Item key="1">
                <Link to='/manage_employee'/>
                <Icon type="team" />
                <span>Manage Employee</span>
            </Menu.Item>
            :
            <Menu.Item key="1">
              <Link to='/'/>
              <Icon type="home" />
              <span>Home</span>
            </Menu.Item>
          }
            <SubMenu
              key="sub1"
              title={
                <span>
                  <Icon type="export" />
                  <span>Leave</span>
                </span>
              }
            >
            { user_role !== "Admin"?
              <Menu.Item key="3">
                  <Link to='/request_leave'/>
                  <Icon type="paper-clip" />
                  <span>Request Leave</span>
              </Menu.Item>
              :null
            }
            { user_role === "Admin"?
              <Menu.Item key="18">
                <Link to='/add_lieu'/>
                <Icon type="paper-clip" />
                <span>Add Lieu</span>
              </Menu.Item>
            :
              <Menu.Item key="15">
                  <Link to='/collect_lieu'/>
                  <Icon type="paper-clip" />
                  <span>Collect Lieu</span>
              </Menu.Item>
            }
            { user_role !== "Admin"?
              <Menu.Item key="17">
                    <Link to='/own_pending'/>
                    <Icon type="question-circle" />
                    <span>Own Pending Leaves</span>
              </Menu.Item>
              :null
            }
            { user_role === "Admin" || user_role === "Supervisor"?
              
                <Menu.Item key="4">
                    <Link to='/leave_history'/>
                    <Icon type="file-search" />
                    <span>Review Leaves</span>
                </Menu.Item>
              :null
            }
            { user_role === "Admin" || user_role === "Supervisor"?
                <Menu.Item key="5">
                    <Link to='/pending_leaves'/>
                    <Icon type="question-circle" />
                    <span>Pending Leaves</span>
                </Menu.Item>
            :null
            }
            { user_role === "Admin" || user_role === "Supervisor"?
                <Menu.Item key="16">
                    <Link to='/pending_lieu'/>
                    <Icon type="question-circle" />
                    <span>Pending Lieu</span>
                </Menu.Item>
            :null
            }
            <Menu.Item key="14">
                {/* <a href='http://leaves.vizuamatix.com:6077/Docs/LeavePolicy.pdf' target='_blank' rel="noopener noreferrer"> */}
                <a href='http://leaves.vizuamatix.com:5000/pdf/leave_policy' target='_blank' rel="noopener noreferrer" >
                <Icon type="file-protect" />
                <span>Leave Policy</span></a>
            </Menu.Item>
            </SubMenu>
            { user_role === "Admin" ?
              <Menu.Item key="6">
                  <Link to='/view_profile'/>
                  <Icon type="eye" />
                  <span>View Profiles</span>
              </Menu.Item>
            :null
            }
            { user_role === "Admin" || user_role === "Supervisor"?
              <Menu.Item key="7">
                  <Link to='/history'/>
                  <Icon type="clock-circle" />
                  <span>History</span>
              </Menu.Item>
            :null
            }
            {/* { user_role === "Admin" ?
              <Menu.Item key="12">
                  <Link to='/attendence'/>
                  <Icon type="solution" />
                  <span>Attendence Sheet</span>
              </Menu.Item>
            :null */}

            <SubMenu
              key="sub2"
              title={
                <span>
                  <Icon type="setting" />
                  <span>Settings</span>
                </span>
              }
            >
            { user_role !== "Admin"?
              <Menu.Item key="8">
                <Link to='/contact_number'/>
                <Icon type="phone" />
                <span>Contact Numbers</span>
              </Menu.Item>
              :null
            }
              <Menu.Item key="9">
                <Link to='/change_password'/>
                <Icon type="lock" />
                <span>Change Password</span>
              </Menu.Item>
              { user_role === "Admin" ?
                <Menu.Item key="11">
                  <Link to='/leave_calender'/>
                  <Icon type="calendar" />
                  <span>Leave Calendar</span>
                </Menu.Item>
              :null
              }
            </SubMenu>
          </Menu>
        </Sider>
        <Layout style={{ marginLeft: 235 }}>
          
          <Layout style={{ position: 'fixed', zIndex: 1000, width: '90%' }}>
              <Header name={user_name} role={user_role} department={user_department} image={user_image} designation={user_des}/>
          </Layout>

          <Layout style={{marginTop:'100px'}} >
            
          <Spin size="large" spinning={this.state.spin}/>
          <Content style={{ margin: '20px', paddingTop: '20PX' }}>
              <Switch>
                <Route exact path='/' component={Dashboard} />
                <Route path='/leave_history' component={LeavesHistory} />
                <Route path='/history' component={History} />
                <Route path='/dashboard' component={Dashboard} />
                <Route path='/request_leave' component={RequestLeave} />
                <Route path='/pending_leaves' component={PendingLeaves} />
                <Route path='/manage_employee' component={ViewEmployee} />
                <Route path='/change_password' component={ChangePassword} />
                <Route path='/view_profile' component={ViewProfile} />
                <Route path='/get_employee' component={ViewOne} /> 
                <Route path='/leave_calender' component={LeaveCalender}/>
                <Route path='/contact_number' component={ContactsNumber}/>
                <Route path='/attendance' component={Attendence}/>
                <Route path='/one_attendance' component={OneAttendence}/>
                <Route path='/add_lieu' component={AddLieu}/>
                <Route path='/collect_lieu' component={CollectLieu}/>
                <Route path='/pending_lieu' component={PendingLieu}/>
                <Route path='/own_pending' component={OwnPending}/>
                <Route path='/error404' component={Error}/>
                <Route path='*' render={() => 
                  (
                    <Redirect to="/error404"/>
                  )
                }/>
              </Switch>
          </Content>
          
          <Footer style={{ textAlign: 'center', height: '0px'}}>VX HRMS (<span className="version">V1.0.2</span>) © Powered By <a href="https://www.vizuamatix.com/" target="_blank" rel="noopener noreferrer" className="footer">VizuaMatix</a> </Footer>
          </Layout>
        </Layout>
      </Layout>
    );
  }else {
    return null
  }
  }
}

const Main = Form.create()(SiderDemo);

export default Main;
   
/* <Breadcrumb style={{ margin: '16px 0' }}>
              <Breadcrumb.Item>User</Breadcrumb.Item>
              <Breadcrumb.Item>Bill</Breadcrumb.Item>
            </Breadcrumb>        */