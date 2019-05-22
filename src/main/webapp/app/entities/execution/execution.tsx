import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './execution.reducer';
import { IExecution } from 'app/shared/model/execution.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExecutionProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Execution extends React.Component<IExecutionProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { executionList, match } = this.props;
    return (
      <div>
        <h2 id="execution-heading">
          Executions
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Execution
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Planned</th>
                <th>Done</th>
                <th>Done Comment</th>
                <th>Diagnostic</th>
                <th>Person</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {executionList.map((execution, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${execution.id}`} color="link" size="sm">
                      {execution.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={execution.planned} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={execution.done} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{execution.doneComment}</td>
                  <td>{execution.diagnostic ? <Link to={`diagnostic/${execution.diagnostic.id}`}>{execution.diagnostic.id}</Link> : ''}</td>
                  <td>{execution.person ? <Link to={`person/${execution.person.id}`}>{execution.person.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${execution.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${execution.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${execution.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ execution }: IRootState) => ({
  executionList: execution.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Execution);
