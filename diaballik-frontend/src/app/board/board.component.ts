import { Component, OnInit, ViewChildren, ElementRef, QueryList, AfterViewInit } from '@angular/core';

import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MyData } from '../mydata';
import { VirtualTimeScheduler } from 'rxjs';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {
  
    // Refers to the #myobjects declared in the html
    // If not a list, just a ViewChild and an ElementRef
    @ViewChildren('myobjects')
    private myobjects: QueryList<ElementRef>;
    ok: boolean;
    
    firstClick: number;
    //secondClick: number[2];
    // dependency injection: the httpclient is a singleton injected through the constructor
    // Same thing for the router and 'data'.
    // Look at the app.module.ts file to see how the HTTP and router modules have been added to be used here and
    // how 'data' as been configured to be an object that can be injected in the different components of the app.
    // constructor parameters that are defined with a visibility are turned as attributes of the class.
    constructor(private http: HttpClient, private router: Router, private data: MyData) {
      this.ok = false;
      this.firstClick = -1;
    }
  
    ngOnInit() {
    }
  
    // this method comes from the interface AfterViewInit.
    // Similarly to the initialize of JavaFX, it is called after the view has been initialised
    // (and the ViewChild, ViewChildren objects OK)
    ngAfterViewInit(): void {
      // 'myobjects' is not an array: it is a query that queries the html for the elements
      // The content of the query is not the objects but a set of references to the objects (have to use 'nativeElement')
      this.myobjects.forEach(myobject => console.log(myobject.nativeElement));
    }
  
    public isOk(): boolean {
      return this.ok;
    }
  
    public setOk(): void {
      this.ok = !this.ok;
    }
    public isAPiecePlayer1(p: number, x: number, y: number): boolean {
      if( (this.data.storage.board.piecesPlayer1[p].cas.positionX === x) && (this.data.storage.board.piecesPlayer1[p].cas.positionY === y) && (this.data.storage.board.board[x*7+ y].isAPiece)  )
      {
        return true;
      }
      else { 
        return false;
      }
    }

    public colorP1Noir(): boolean {
      return this.data.storage.player1.color === 'NOIR';
    }

    public nameP1(): boolean {
      return this.data.storage.player1.name;
    }
    
    public nameP2(): boolean {
      return this.data.storage.player2.name;
    }

    public haveABallP1(p: number): boolean {
      if(this.data.storage.board.piecesPlayer1[p].haveABall)
      {
        return true;
      }
      else { 
        return false;
      }
    }

    public haveABallP2(p: number): boolean {
      if(this.data.storage.board.piecesPlayer2[p].haveABall)
      {
        return true;
      }
      else { 
        return false;
      }
    }

    public undo(): void {
      this.http.post(`game/undo`, {}, {}).subscribe(jsonBoard => {
          this.data.storage = jsonBoard;
          this.router.navigate['/board'];
      });
    }

    public redo(): void {
      this.http.post(`game/redo`, {}, {}).subscribe(jsonBoard => {
          this.data.storage = jsonBoard;
          this.router.navigate['/board'];
      });
    }

    public endTurn(): void {
      this.http.post(`game/endTurn`, {}, {}).subscribe(jsonBoard => {
          this.data.storage = jsonBoard;
          this.router.navigate['/board'];
      });
    }
    public isAPiecePlayer2(p: number, x: number, y: number): boolean {
      if( (this.data.storage.board.piecesPlayer2[p].cas.positionX === x) && (this.data.storage.board.piecesPlayer2[p].cas.positionY === y) && (this.data.storage.board.board[x*7+ y].isAPiece)  )
      {
        return true;
      }
      else { 
        return false;
      }
    }

    

    public getIdPiece(x: number, y: number): number {

      var p;
      for(p=0;p<7; p++){
        if( (this.data.storage.board.piecesPlayer1[p].cas.positionX === x) && (this.data.storage.board.piecesPlayer1[p].cas.positionY === y) && (this.data.storage.board.board[x*7+ y].isAPiece)  )
        {
          return p;
        }
        if( (this.data.storage.board.piecesPlayer2[p].cas.positionX === x) && (this.data.storage.board.piecesPlayer2[p].cas.positionY === y) && (this.data.storage.board.board[x*7+ y].isAPiece)  )
        {
          return p;
        }
     }
    }
    public rightClick(event: MouseEvent): void {
      if(this.firstClick == -1) {
        this.firstClick = this.getIdPiece(Number((event.currentTarget as Element).getAttribute('data-x')),Number((event.currentTarget as Element).getAttribute('data-y')));
        if(this.firstClick == undefined){
          this.firstClick = -1;
        }
      }
      else {
        if(((this.data.storage.board.piecesPlayer1[this.firstClick].haveABall == true) && (this.data.storage.player1.myTurn == true) || 
        ((this.data.storage.board.piecesPlayer2[this.firstClick].haveABall == true) && (this.data.storage.player2.myTurn == true))))
        { 
          this.http.post(`game/moveBall/${this.firstClick}/${this.getIdPiece(Number((event.currentTarget as Element).getAttribute('data-x')),Number((event.currentTarget as Element).getAttribute('data-y')))}`, {}, {}).
        // This is reactive programming + future object:
        // the request is sent. You can subscribe to (wait for) the returned data as follows:
        subscribe(jsonBoard => {
          this.data.storage = jsonBoard;
          this.router.navigate['/board'];
          this.firstClick = -1;
        });
        }
        else {
          this.http.post(`game/movePiece/${this.firstClick}/${(event.currentTarget as Element).getAttribute('data-x')}/${(event.currentTarget as Element).getAttribute('data-y')}`, {}, {}).
          subscribe(jsonBoard => {
            this.data.storage = jsonBoard;
            this.router.navigate['/board'];
            this.firstClick = -1;
          });
        }
        this.firstClick = -1;
      }
  }
    public win(): boolean{
      return this.data.storage.win;
    }
    public p1Win(): boolean{
      return this.data.storage.player1.myTurn;
    }
    public movePiece(): void {
      
      
    }
}
