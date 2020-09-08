import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Login from './auth/Login';
import Forgot from './auth/Forgot';
import FirstLogin from './auth/FirstLogin';
import Sidebar from './container/Sidebar';

class App extends Component {
  
    render(){
    return (
      <BrowserRouter>
        <div className="App" style={{height: '100%'}}>
          <Switch>
            <Route path='/login' component={Login} />
            <Route path='/forgot' component={Forgot} />
            <Route path='/first_login' component={FirstLogin} />
            <Route path='/' component={Sidebar} />
          </Switch>       
        </div>
      </BrowserRouter>
    )
  }
}

export default App; 