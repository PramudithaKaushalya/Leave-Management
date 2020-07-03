import React, { Component } from 'react';
import axios from 'axios';
import 'antd/dist/antd.css';
import {Table, Button, Icon, Input, Card, message } from 'antd';
import Highlighter from 'react-highlight-words';

class Attendence extends Component {
    
    componentWillMount(){
        this.setState({
            data : []
        })
        axios.get('http://localhost:5000/leave_count/profile', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            console.log("res", res.data);
            if (res.data.success === true) {
              this.setState({
                data : res.data.list
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
        searchText: '',
        data : []
    };
    
    getColumnSearchProps = dataIndex => ({
      filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
          <div style={{ padding: 8 }}>
            <Input
              ref={node => {
                this.searchInput = node;
              }}
              placeholder={`Search ${dataIndex}`}
              value={selectedKeys[0]}
              onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
              onPressEnter={() => this.handleSearch(selectedKeys, confirm)}
              style={{ width: 188, marginBottom: 8, display: 'block' }}
            />
            <Button
              type="primary"
              onClick={() => this.handleSearch(selectedKeys, confirm)}
              icon="search"
              size="small"
              style={{ width: 90, marginRight: 8 }}
            >
              Search
            </Button>
            <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{ width: 90 }}>
              Reset
            </Button>
          </div>
        ),
        filterIcon: filtered => (
          <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
        ),
        onFilter: (value, record) =>
          record[dataIndex]
            .toString()
            .toLowerCase()
            .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
          if (visible) {
            setTimeout(() => this.searchInput.select());
          }
        },
        render: text => (
          <Highlighter
            highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
            searchWords={[this.state.searchText]}
            autoEscape
            textToHighlight={text.toString()}
          />
      ),
    });
    
    handleSearch = (selectedKeys, confirm) => {
      confirm();
      this.setState({ searchText: selectedKeys[0] });
    };
    
    handleReset = clearFilters => {
      clearFilters();
      this.setState({ searchText: '' });
    };   
    
    handleClick = (emp, e) => {
      e.preventDefault();
      console.log("aaaaa",emp);
      localStorage.setItem("leave_count", emp.id);
      this.props.history.push('/one_attendence');
    }

    render() {

        const columns = [
          {
            title: 'Name',
            key: '0',
            dataIndex: 'name'
          },
          {
            title: 'Casual',
            key: '1',
            dataIndex: 'casual'
          },    
          {
            title: 'Medical',
            key: '2',
            dataIndex: 'medical'
          },
          {
            title: 'Maternity',
            key: '3',
            dataIndex: 'maternity'
          },
          {
            title: 'Paternity',
            key: '4',
            dataIndex: 'paternity'
          },
          {
            title: 'Annual',
            key: '5',
            dataIndex: 'annual'
          },
          {
            title: 'Lieu',
            key: '6',
            dataIndex: 'lieu'
          },
          {
            title: 'Special',
            key: '7',
            dataIndex: 'special'
          },
          {
            title: 'Cover Up',
            key: '8',
            dataIndex: 'coverup'
          },
          {
            title: 'No Pay',
            key: '9',
            dataIndex: 'nopay'
          },
          {
            dataIndex: '',
            key: '10',
            render: (emp) => <Icon type="file-search" onClick={this.handleClick.bind(this, emp)} style={{color:'#368DC5'}}/>,
          },
        ];

        return (

          <div>
            <Card hoverable='true'>
              <Table rowKey={record => record.id} columns={columns} dataSource={this.state.data}  pagination={{ pageSize: 2 }} size="middle" />
            </Card>
          </div>
        )
    }
}    


export default Attendence;