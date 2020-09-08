import React, { Component } from 'react';
import axios from '../../config/axios'
import 'antd/dist/antd.css';
import {Table, Button, Icon, Input, Card, message, Spin, Typography } from 'antd';
import Highlighter from 'react-highlight-words';
import './index.css';

const { Text } = Typography;

class ViewProfile extends Component {
    
    componentWillMount(){
        this.setState({
            data : []
        })
        axios.get('leave_count/profile', 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
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
      localStorage.setItem("leave_count", emp.id);
      this.props.history.push('/get_employee');
    }

    render() {

        const columns = [
          {
            title: 'Name',
            key: '0',
            dataIndex: 'name',
            ...this.getColumnSearchProps('name'),
          },
          {
            title: 'Casual',
            key: '1',
            dataIndex: 'casual',
            align: 'center'
          },    
          {
            title: 'Medical',
            key: '2',
            dataIndex: 'medical',
            align: 'center'
          },
          {
            title: 'Annual',
            key: '3',
            dataIndex: 'annual',
            align: 'center'
          },
          {
            title: 'Lieu',
            key: '4',
            dataIndex: 'lieu',
            align: 'center'
          },
          {
            title: 'Special',
            key: '5',
            dataIndex: 'special',
            align: 'center'
          },
          {
            title: 'Maternity',
            key: '6',
            dataIndex: 'maternity',
            align: 'center'
          },
          {
            title: 'Paternity',
            key: '7',
            dataIndex: 'paternity',
            align: 'center'
          },
          {
            title: 'Cover Up',
            key: '8',
            dataIndex: 'coverup',
            align: 'center'
          },
          {
            title: 'No Pay',
            key: '9',
            dataIndex: 'nopay',
            align: 'center',
            render: text => <Text type="danger">{text}</Text>,
          },
          {
            dataIndex: '',
            key: '10',
            render: (emp) => <Icon type="file-search" onClick={this.handleClick.bind(this, emp)} style={{color:'#368DC5'}}/>,
          },
        ];

        return (

          <div>
          { this.state.data !== 0? 
            <Card type="inner" title="View Summary" hoverable='true'>
              <Table rowKey={record => record.id} columns={columns} dataSource={this.state.data}  pagination={{ pageSize: 10 }} size="middle" />
            </Card>
            : 
            <div className="example">
              <Spin size="large" />
            </div>
            } 
          </div>
        )
    }
}    


export default ViewProfile;