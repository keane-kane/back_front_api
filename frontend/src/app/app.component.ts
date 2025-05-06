import { Component } from '@angular/core';
import { AuthService } from './core/auth/auth.service';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './shared/components/header/header.component';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, SidebarComponent, MatSidenavModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Université Cheikh Hamidou Kane';
  isSidenavOpen = true;

  constructor(public authService: AuthService) {}

  toggleSidenav(): void {
    this.isSidenavOpen = !this.isSidenavOpen;
  }
}
