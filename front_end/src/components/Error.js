import React from 'react';
import 'antd/dist/antd.css';
import axios from '../config/axios';
import { Calendar, Card, Modal, Form, Input, message, Badge, Icon } from 'antd';

const { confirm } = Modal;

const ReasonForm = Form.create({ name: 'form_in_modal' })(

  class extends React.Component {
    render() {
      const { visible, onCancel, onCreate, form, date } = this.props;
      const { getFieldDecorator } = form;
      const txt = `Set leave on ${date}`
      return (
        <Modal
          visible={visible}
          title={txt}
          okText="Save"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form layout="vertical">
            <Form.Item label="Reason for give leave">
              {getFieldDecorator('reason', {
                rules: [{ required: true, message: 'Please input the reason' }],
              })(<Input type="textarea"/>)}
            </Form.Item>
          </Form>
        </Modal>
      );
    }
  },
);

class LeaveCalender extends React.Component {

  componentWillMount () {
    this.reload();
  }
  
  reload () {
    axios.get('calender/all', 
    {
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem("header")
        }
    })
    .then(res => {
      if(res.data.success === true){
        this.setState({
          saveDates : res.data.list
        });
      }  else{
        message.error(res.data.message); 
      }
    }).catch(e => {
      console.log(e.response);
    })

    axios.get('calender/dates', 
    {
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem("header")
        }
    })
    .then(res => {
      if(res.data.success === true){
        this.setState({
          dates : res.data.list
        });
      }  else{
        message.error(res.data.message); 
      }
    }).catch(e => {
      console.log(e.response);
    })
  }

  state = {
    saveDates: [],
    dates: [],
    selectedValue: null,
    visible: false
  };

  onSelect = value => {
    if(!this.state.dates.includes(value.format('YYYY-MM-DD'))){
      this.setState({
        selectedValue: value.format('YYYY-MM-DD'),
        visible: true
      })
    }
  };

  handleCancel = () => {
    const { form } = this.formRef.props;
    form.resetFields(); 
    this.setState({ 
      visible: false
    });
  };

  handleSet = () => {
    const { form } = this.formRef.props;
    form.validateFields((err, values) => {

      const calendar = {
        date: this.state.selectedValue,  
        reason : values.reason
      }

      if (!err) {
        axios.post('calender/set_date', 
        calendar,
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
            if(res.data.success === true){
              message.success(res.data.message);
              this.reload();
              this.handleCancel();
            }  else{
              message.error(res.data.message); 
            }
        }).catch(e => {
          message.error(e.response.data.error); 
        })
        
      }
    });
  }

  saveFormRef = formRef => {
    this.formRef = formRef;
  };

  showConfirm = (day) =>{
    console.log(day.id)
    this.setState({
      visible: false
    })
    confirm({
      title: 'Do you want to remove this holiday ?',
      okText: 'Remove',
      okType: 'danger',
      onOk: () => {
        axios.get('calender/delete/'+day.id, 
        {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("header")
            }
        })
        .then(res => {
              if(res.data.success === true){
                message.success(res.data.message);
                this.reload();
                this.handleCancel();
              }  else{
                message.error(res.data.message); 
              }
        }).catch( e => {
            message.error(e.response.data.error);
        })
      },
      onCancel() {
      },
    });
  }

  dateCellRender = (value) => {
    return (
          <ul className="events">
            {this.state.saveDates.map(item => (
              item.date === value.format('YYYY-MM-DD')?
              <li key={item.date}>
                <Badge status='success' text={item.reason} />
                <Badge status='warning' text={item.createdBy} />
                <Badge status='warning' text={item.createdAt} />
                <Icon type="delete" onClick={this.showConfirm.bind(this, item)}  theme="twoTone" twoToneColor='#EE204D'/>
              </li>
              :null
            ))}
          </ul>
        );
  }

  render() {
    return (
      <div>
        <Card>
          <Calendar onSelect={this.onSelect} dateCellRender={this.dateCellRender} />
        </Card>

        <ReasonForm
          wrappedComponentRef={this.saveFormRef}
          visible={this.state.visible}
          onCancel={this.handleCancel}
          onCreate={this.handleSet}
          date={this.state.selectedValue}
        />
      </div>
    );
  }
}


const WrappedLeaveCalender = Form.create({ name: 'calender' })(LeaveCalender);

export default WrappedLeaveCalender;