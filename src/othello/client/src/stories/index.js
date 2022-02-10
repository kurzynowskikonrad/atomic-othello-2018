import React from 'react';

import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { linkTo } from '@storybook/addon-links';

import { Button, Welcome } from '@storybook/react/demo';

import Row from '../components/Row'
import Board from '../components/Board'
import Game from '../components/Game'

storiesOf('Welcome', module).add('to Storybook', () => <Welcome showApp={linkTo('Button')} />);

storiesOf('Row', module)
  .add('with empty state', () => <div className="RowContainer"><Row/></div>)
  .add('with default state', () => <div className="RowContainer"><Row pieces={[0, 0, 0, 1, 2, 0, 0, 0]}/></div>);

storiesOf('Board', module)
  .add('with empty state', () => <Board data={[]}/>)
  .add('with default state', () => <Board data={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                 [0, 0, 0, 0, 0, 0, 0, 0],
                                                 [0, 0, 0, 0, 0, 0, 0, 0],
                                                 [0, 0, 0, 1, 0, 0, 0, 0],
                                                 [0, 0, 0, 0, 0, 0, 0, 0],
                                                 [0, 0, 0, 0, 0, 0, 0, 0],
                                                 [0, 0, 0, 0, 0, 0, 0, 0],
                                                 [0, 0, 0, 0, 0, 0, 0, 0]]}/>);


storiesOf('Game', module)
  .add('with empty state', () => <Game/>)
  .add('with player one active', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 1, 2, 0, 0, 0],
                                                     [0, 0, 0, 2, 1, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p1Connected={true}
                                          p2Name="Peace Turtles"
                                          p2Connected={false}
                                          lastMove={[3, 3]}
                                          currentPlayer="player-one"/>)

  .add('with player two active', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 1, 1, 1, 0, 0],
                                                     [0, 0, 0, 2, 1, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0],
                                                     [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p1Connected={false}
                                          p2Name="Peace Turtles"
                                          p2Connected={true}
                                          currentPlayer="player-two"/>)
  .add('ended with a tie', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 1, 1, 1, 0, 0],
                                                  [0, 0, 0, 2, 1, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p2Name="Peace Turtles"
                                          currentPlayer="none"
                                          status="game-over"
                                          result="tied" />)
  .add('with player one won', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 1, 1, 1, 0, 0],
                                                  [0, 0, 0, 2, 1, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p2Name="Peace Turtles"
                                          currentPlayer="none"
                                          status="game-over"
                                          result="player-one-won" />)
  .add('with player one won due to error', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 1, 1, 1, 0, 0],
                                                  [0, 0, 0, 2, 1, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p2Name="Peace Turtles"
                                          currentPlayer="none"
                                          status="game-over"
                                          result="player-one-won-due-to-error" />)
  .add('with player two won', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 1, 1, 1, 0, 0],
                                                  [0, 0, 0, 2, 1, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p2Name="Peace Turtles"
                                          currentPlayer="none"
                                          status="game-over"
                                          result="player-two-won" />)
  .add('with player two won due to error', () => <Game board={[[0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 1, 1, 1, 0, 0],
                                                  [0, 0, 0, 2, 1, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0],
                                                  [0, 0, 0, 0, 0, 0, 0, 0]]}
                                          p1Name="Space Cats"
                                          p2Name="Peace Turtles"
                                          currentPlayer="none"
                                          status="game-over"
                                          result="player-two-won-due-to-error" />);
