import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.authService.isAuthenticated()) {
      // Check if route has specific role requirements
      const requiredRole = route.data['role'] as string;
      if (requiredRole && !this.authService.hasRole(requiredRole)) {
        // User doesn't have the required role, redirect to dashboard
        this.router.navigate(['/dashboard']);
        return false;
      }
      return true;
    }
    
    // Not authenticated, redirect to login
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
