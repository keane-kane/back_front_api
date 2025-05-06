import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '../../../core/auth/auth.service';
import { Router } from '@angular/router';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule,MatMenuModule, MatIconModule, MatToolbarModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Output() menuToggled = new EventEmitter<void>();
  
  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  toggleMenu(): void {
    this.menuToggled.emit();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  navigateToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  getUserFullName(): string {
    const user = this.authService.currentUserValue;
    return user ? user.fullName : 'Utilisateur';
  }

  getUserRole(): string {
    const user = this.authService.currentUserValue;
    if (!user || !user.roles.length) return '';
    
    const roleMap: { [key: string]: string } = {
      'ROLE_ADMIN': 'Administrateur',
      'ROLE_ADMINISTRATIVE': 'Personnel Administratif',
      'ROLE_INSTRUCTOR': 'Formateur',
      'ROLE_STUDENT': 'Étudiant'
    };
    
    return user.roles.map(role => roleMap[role] || role).join(', ');
  }
}
