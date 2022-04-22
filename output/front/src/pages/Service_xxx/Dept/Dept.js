import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Button, Col, Form, Input, Row } from 'antd';
import Panel from '../../../components/Panel';
import { DEPT_LIST } from '../../../actions/dept';
import Grid from '../../../components/Sword/Grid';

const FormItem = Form.Item;

@connect(({ dept, loading }) => ({
  dept,
  loading: loading.models.dept,
}))
@Form.create()
class Dept extends PureComponent {
  // ============ 查询 ===============
  handleSearch = params => {
    const { dispatch } = this.props;
    dispatch(DEPT_LIST(params));
  };

  // ============ 查询表单 ===============
  renderSearchForm = onReset => {
    const { form } = this.props;
    const { getFieldDecorator } = form;

    return (
      <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
        <Col md={6} sm={24}>
          <FormItem label="查询名称">
            {getFieldDecorator('name')(<Input placeholder="查询名称" />)}
          </FormItem>
        </Col>
        <Col>
          <div style={{ float: 'right' }}>
            <Button type="primary" htmlType="submit">
              查询
            </Button>
            <Button style={{ marginLeft: 8 }} onClick={onReset}>
              重置
            </Button>
          </div>
        </Col>
      </Row>
    );
  };

  render() {
    const code = 'dept';

    const {
      form,
      loading,
      dept: { data },
    } = this.props;

    const columns = [
      {
        title: '主键',
        dataIndex: 'id',
      },
      {
        title: '父主键',
        dataIndex: 'parentId',
      },
      {
        title: '祖级列表',
        dataIndex: 'ancestors',
      },
      {
        title: '部门名',
        dataIndex: 'deptName',
      },
      {
        title: '部门全称',
        dataIndex: 'fullName',
      },
      {
        title: '排序',
        dataIndex: 'sort',
      },
      {
        title: '备注',
        dataIndex: 'remark',
      },
    ];

    return (
      <Panel>
        <Grid
          code={code}
          form={form}
          onSearch={this.handleSearch}
          renderSearchForm={this.renderSearchForm}
          loading={loading}
          data={data}
          columns={columns}
        />
      </Panel>
    );
  }
}
export default Dept;
