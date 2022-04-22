import React, { PureComponent } from 'react';
import { Form, Input, Card, Button } from 'antd';
import { connect } from 'dva';
import Panel from '../../../components/Panel';
import styles from '../../../layouts/Sword.less';
import { DEPT_SUBMIT } from '../../../actions/dept';

const FormItem = Form.Item;

@connect(({ loading }) => ({
  submitting: loading.effects['dept/submit'],
}))
@Form.create()
class DeptAdd extends PureComponent {
  handleSubmit = e => {
    e.preventDefault();
    const { dispatch, form } = this.props;
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        dispatch(DEPT_SUBMIT(values));
      }
    });
  };

  render() {
    const {
      form: { getFieldDecorator },
      submitting,
    } = this.props;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 },
        md: { span: 10 },
      },
    };

    const action = (
      <Button type="primary" onClick={this.handleSubmit} loading={submitting}>
        提交
      </Button>
    );

    return (
      <Panel title="新增" back="/service_xxx/dept" action={action}>
        <Form hideRequiredMark style={{ marginTop: 8 }}>
          <Card className={styles.card} bordered={false}>
            <FormItem {...formItemLayout} label="主键">
              {getFieldDecorator('id', {
                rules: [
                  {
                    required: true,
                    message: '请输入主键',
                  },
                ],
              })(<Input placeholder="请输入主键" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="父主键">
              {getFieldDecorator('parentId', {
                rules: [
                  {
                    required: true,
                    message: '请输入父主键',
                  },
                ],
              })(<Input placeholder="请输入父主键" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="祖级列表">
              {getFieldDecorator('ancestors', {
                rules: [
                  {
                    required: true,
                    message: '请输入祖级列表',
                  },
                ],
              })(<Input placeholder="请输入祖级列表" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="部门名">
              {getFieldDecorator('deptName', {
                rules: [
                  {
                    required: true,
                    message: '请输入部门名',
                  },
                ],
              })(<Input placeholder="请输入部门名" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="部门全称">
              {getFieldDecorator('fullName', {
                rules: [
                  {
                    required: true,
                    message: '请输入部门全称',
                  },
                ],
              })(<Input placeholder="请输入部门全称" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="排序">
              {getFieldDecorator('sort', {
                rules: [
                  {
                    required: true,
                    message: '请输入排序',
                  },
                ],
              })(<Input placeholder="请输入排序" />)}
            </FormItem>
            <FormItem {...formItemLayout} label="备注">
              {getFieldDecorator('remark', {
                rules: [
                  {
                    required: true,
                    message: '请输入备注',
                  },
                ],
              })(<Input placeholder="请输入备注" />)}
            </FormItem>
          </Card>
        </Form>
      </Panel>
    );
  }
}

export default DeptAdd;
