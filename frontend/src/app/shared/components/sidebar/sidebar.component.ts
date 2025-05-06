import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/auth/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink, RouterModule, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';

interface SidebarItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[];
}

@Component({
  selector: 'app-sidebar',
    standalone: true,
    imports: [CommonModule, MatIconModule, MatListModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  menuItems: SidebarItem[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.initializeMenu();
  }

  private initializeMenu(): void {
    const allMenuItems: SidebarItem[] = [
      {
        label: 'Tableau de Bord',
        icon: 'dashboard',
        route: '/dashboard'
      },
      {
        label: 'Communication',
        icon: 'forum',
        route: '/reports',
        roles: ['ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_INSTRUCTOR']
      },
      {
        label: 'Administration',
        icon: 'work',
        route: '/administration',
        roles: ['ROLE_ADMIN', 'ROLE_ADMINISTRATIVE']
      },
      {
        label: 'Insertion Professionnelle',
        icon: 'business_center',
        route: '/insertion',
        roles: ['ROLE_ADMIN', 'ROLE_ADMINISTRATIVE']
      },
      {
        label: 'Formation',
        icon: 'school',
        route: '/training'
      },
      {
        label: 'Profil Étudiant',
        icon: 'person',
        route: '/student/profile',
        roles: ['ROLE_ADMIN', 'ROLE_ADMINISTRATIVE', 'ROLE_STUDENT']
      }
    ];

    // Filter menu items based on user roles
    this.menuItems = allMenuItems.filter(item => {
      // If no roles specified, show to everyone
      if (!item.roles) return true;
      
      // Otherwise check if user has required role
      return item.roles.some(role => this.authService.hasRole(role));
    });
  }
}
