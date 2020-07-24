import React from 'react';
import 'antd/dist/antd.css';
import {Button } from 'antd';
import '../index.css';

class Error extends React.Component {


  state = {
  };

  onSelect = value => {
  
  };

  render() {
    return (
      <div class="bg-img">
        <center>
          <span style={{fontSize:'35px'}}>Page Not Found</span><br/>
          <div class="error-img">
          </div>
          <Button key="1" type="primary" href='/' icon="home" size='large'> Home </Button>
        </center>
      </div>
        
    );
  }
}

export default Error;