import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/models/user.model';
import { ApiService } from '../../core/services/api.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatProgressSpinnerModule ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  recentReports: any[] = [];
  upcomingEvents: any[] = [];
  statistics: any = {};
  loading = true;

  constructor(
    private authService: AuthService,
    private apiService: ApiService
  ) { }

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      console.log('Current user loaded:', user);
      if (user) {
        this.loadDashboardData();
      }
    });

    
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    // Load data based on user role
    if (this.currentUser) {
      if (this.currentUser.isAdmin || this.currentUser.isAdministrative) {
        this.loadAdminDashboard();
      } else if (this.currentUser.isInstructor) {
        this.loadInstructorDashboard();
      } else if (this.currentUser.isStudent) {
        this.loadStudentDashboard();
      } else {
        this.loadDefaultDashboard();
      }
    }
  }

  loadAdminDashboard(): void {
    // Load administrative stats and recent data
    this.apiService.get('/api/reports/recent').subscribe({
      next: (data: any) => {
        this.recentReports = data;
        console.log(data);
        
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading reports', error);
        this.loading = false;
      }
    });

    this.apiService.get('/api/dashboard/statistics').subscribe({
      next: (data: any) => {
        this.statistics = data;
      },
      error: (error) => {
        console.error('Error loading statistics', error);
      }
    });
  }

  loadInstructorDashboard(): void {
    // Load instructor's upcoming courses and students
    this.apiService.get('/api/training/instructor/upcoming').subscribe({
      next: (data: any) => {
        this.upcomingEvents = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading upcoming events', error);
        this.loading = false;
      }
    });
  }

  loadStudentDashboard(): void {
    // Load student's courses and notifications
    this.apiService.get('/api/training/student/schedule').subscribe({
      next: (data: any) => {
        this.upcomingEvents = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading student schedule', error);
        this.loading = false;
      }
    });
  }

  loadDefaultDashboard(): void {
    // Load basic data for other roles
    this.apiService.get('/api/dashboard/announcements').subscribe({
      next: (data: any) => {
        this.recentReports = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading announcements', error);
        this.loading = false;
      }
    });
  }
}
