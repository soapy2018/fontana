import React, { PureComponent } from 'react';
import router from 'umi/router';
import { Form, Card, Button } from 'antd';
import { connect } from 'dva';
import Panel from '../../../components/Panel';
import styles from '../../../layouts/Sword.less';
import { DEPT_DETAIL } from '../../../actions/dept';

const FormItem = Form.Item;

@connect(({ dept }) => ({
  dept,
}))
@Form.create()
class DeptView extends PureComponent {
  componentWillMount() {
    const {
      dispatch,
      match: {
        params: { id },
      },
    } = this.props;
    dispatch(DEPT_DETAIL(id));
  }

  handleEdit = () => {
    const {
      match: {
        params: { id },
      },
    } = this.props;
    router.push(`/service_xxx/dept/edit/`);
  };

  render() {
    const {
      dept: { detail },
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
      <Button type="primary" onClick={this.handleEdit}>
        修改
      </Button>
    );

    return (
      <Panel title="查看" back="/service_xxx/dept" action={action}>
        <Form hideRequiredMark style={{ marginTop: 8 }}>
          <Card className={styles.card} bordered={false}>
            <FormItem {...formItemLayout} label="主键">
              <span>{detail.id}</span>
            </FormItem>
            <FormItem {...formItemLayout} label="父主键">
              <span>{detail.parentId}</span>
            </FormItem>
            <FormItem {...formItemLayout} label="祖级列表">
              <span>{detail.ancestors}</span>
            </FormItem>
            <FormItem {...formItemLayout} label="部门名">
              <span>{detail.deptName}</span>
            </FormItem>
            <FormItem {...formItemLayout} label="部门全称">
              <span>{detail.fullName}</span>
            </FormItem>
            <FormItem {...formItemLayout} label="排序">
              <span>{detail.sort}</span>
            </FormItem>
            <FormItem {...formItemLayout} label="备注">
              <span>{detail.remark}</span>
            </FormItem>
          </Card>
        </Form>
      </Panel>
    );
  }
}
export default DeptView;
