import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { ReportsComponent } from './features/communication/reports/reports.component';
import { LoginComponent } from './features/auth/login/login.component';
import { AuthGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'dashboard',component: DashboardComponent, canActivate: [AuthGuard]   },
  {
    path: 'reports',
    loadComponent: () => import('./features/communication/reports/reports.component').then(m => m.ReportsComponent),
    canActivate: [AuthGuard]
  },
    {
      path: 'documents',
      loadComponent: () => import('./features/administration/document-management/document-management.component').then(m => m.DocumentManagementComponent),
      canActivate: [AuthGuard],
      data: { role: 'ROLE_ADMINISTRATIVE' }
    },
    {
        path: 'student/profile',
        loadComponent: () => import('./features/student/student-profile/student-profile.component').then(m => m.StudentProfileComponent),
        canActivate: [AuthGuard]
      },
      {
        path: 'tracking',
        loadComponent: () => import('./features/insertion/student-tracking/student-tracking.component').then(m => m.StudentTrackingComponent),
        canActivate: [AuthGuard]
      },
      {
        path: 'schedule',
        loadComponent: () => import('./features/training/course-scheduling/course-scheduling.component').then(m => m.CourseSchedulingComponent),
        canActivate: [AuthGuard]
      },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: 'dashboard' }
];
