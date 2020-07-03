import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Login from './auth/Login';
import Forgot from './auth/Forgot';
import Sidebar from './container/Sidebar';
import Error_404 from './components/Error';

class App extends Component {
  
    render(){
    return (
      <BrowserRouter>
        <div className="App" style={{height: '100%'}}>
          <Switch>
            <Route path='/login' component={Login} />
            <Route path='/forgot' component={Forgot} />
            <Route path='/' component={Sidebar} />
            <Route path='/pages/error-404' component={Error_404}/>
          </Switch>       
        </div>
      </BrowserRouter>
    )
  }
}

export default App; 