import React, { PureComponent } from 'react';
import { Form, Input, Card, Button } from 'antd';
import { connect } from 'dva';
import Panel from '../../../components/Panel';
import styles from '../../../layouts/Sword.less';
import { DEPT_DETAIL, DEPT_SUBMIT } from '../../../actions/dept';

const FormItem = Form.Item;

@connect(({ dept, loading }) => ({
  dept,
  submitting: loading.effects['dept/submit'],
}))
@Form.create()
class DeptEdit extends PureComponent {
  componentWillMount() {
    const {
      dispatch,
      match: {
        params: { id },
      },
    } = this.props;
    dispatch(DEPT_DETAIL(id));
  }

  handleSubmit = e => {
    e.preventDefault();
    const {
      dispatch,
      match: {
        params: { id },
      },
      form,
    } = this.props;
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        const params = {
          id,
          ...values,
        };
        console.log(params);
        dispatch(DEPT_SUBMIT(params));
      }
    });
  };

  render() {
    const {
      form: { getFieldDecorator },
      dept: { detail },
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
      <Panel title="修改" back="/service_xxx/dept" action={action}>
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
                initialValue: detail.id,
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
                initialValue: detail.parentId,
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
                initialValue: detail.ancestors,
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
                initialValue: detail.deptName,
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
                initialValue: detail.fullName,
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
                initialValue: detail.sort,
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
                initialValue: detail.remark,
              })(<Input placeholder="请输入备注" />)}
            </FormItem>
          </Card>
        </Form>
      </Panel>
    );
  }
}

export default DeptEdit;
