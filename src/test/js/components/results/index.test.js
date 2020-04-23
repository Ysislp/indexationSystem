import React from 'react';
import { Provider } from 'react-redux';
import { MemoryRouter } from 'react-router-dom'
import Enzyme, { shallow, render, mount } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import thunk from 'redux-thunk';
import configureMockStore from 'redux-mock-store';
import renderer from 'react-test-renderer';
import ResultsApp from '../../../../main/js/components/results';

Enzyme.configure({ adapter: new Adapter() });

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);



describe('Results component Test', () => {

  let store;
  let getResults;;
  let deleteUrl;
  let component;
  
  beforeEach(() => {

    store = mockStore({
      results: { 
        urls: [
          {
            id: 1,
            url: "http://google.com"
          },
        ]
      }
    })

    getResults = jest.fn();
    deleteUrl = jest.fn();

    component = renderer.create(
      <Provider store={store}>
        <MemoryRouter>
          <ResultsApp />
        </MemoryRouter>
      </Provider>
    )
  })

  it('Snapshot Results', () => {
    expect(component.toJSON()).toMatchSnapshot();
  });
  
})