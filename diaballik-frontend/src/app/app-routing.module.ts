import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BoardComponent } from './board/board.component';
import {MenuComponent } from './menu/menu.component';


const routes: Routes = [
  { path: 'menu', component: MenuComponent },
  { path: 'board', component: BoardComponent },
  { path: '', redirectTo: '/menu',pathMatch: 'full'},
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
