
import { Component, OnInit, ViewChildren, ElementRef, QueryList, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MyData } from '../mydata';
import { VirtualTimeScheduler } from 'rxjs';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent implements OnInit {
  public nameP1: string;
  public nameP2: string;
  public colorP1: string;
  public gamemode: boolean;
  public level: string;

  constructor(private http: HttpClient, private router: Router, private data: MyData) { }

  ngOnInit() {
    this.nameP1= 'Player 1';
    this.nameP2= 'Player 2';
    this.colorP1= 'NOIR';
    this.gamemode= true;
    this.level = 'noob';
  }

  public launchGameHuman(): void{
    this.http.post(`game/configureGamePlayer/${this.nameP1}/${this.colorP1}/${this.nameP2}`, {}, {}).subscribe(jsonBoard => {
      this.data.storage = jsonBoard;
      this.router.navigate['/board'];
  });
  }

  public launchGameAI(): void{
    this.http.post(`game/configureGameIA/${this.nameP1}/${this.colorP1}/${this.level}`, {}, {}).subscribe(jsonBoard => {
      this.data.storage = jsonBoard;
      this.router.navigate['/board'];
  });
  }


  public changemode():void{
    this.gamemode = !this.gamemode;
  }

}
