import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [
        CommonModule,
        ReactiveFormsModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatDatepickerModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatTabsModule,
        MatProgressSpinnerModule,
        MatChipsModule,
        MatExpansionModule
  ],
  templateUrl: './student-profile.component.html',
  styleUrls: ['./student-profile.component.scss']
})
export class StudentProfileComponent implements OnInit {
  studentForm: FormGroup;
  studentData: any = null;
  courses: any[] = [];
  diplomas: any[] = [];
  otherTrainings: any[] = [];
  loading = true;
  formMode = 'view';
  isAdmin = false;
  isStudent = false;
  searchForm: FormGroup;
  searchResults: any[] = [];
  searching = false;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService
  ) {
    this.studentForm = this.formBuilder.group({
      id: [''],
      ine: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      formation: ['', Validators.required],
      promotion: ['', Validators.required],
      startYear: ['', [Validators.required, Validators.min(1900), Validators.max(2100)]],
      endYear: ['', [Validators.min(1900), Validators.max(2100)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.pattern('^[0-9]{9,15}$')]
    });

    this.searchForm = this.formBuilder.group({
      query: ['', Validators.required]
    });

    const currentUser = this.authService.currentUserValue;
    this.isAdmin = currentUser?.isAdmin || currentUser?.isAdministrative || false;
    this.isStudent = currentUser?.isStudent || false;
  }

  ngOnInit(): void {
    if (this.isStudent) {
      this.loadStudentProfile();
    }
  }

  loadStudentProfile(id?: number) {
    this.loading = true;
    
    let endpoint = '/api/students/profile';
    if (id && this.isAdmin) {
      endpoint = `/api/students/${id}`;
    }
    
    this.apiService.get(endpoint).subscribe({
      next: (data: any) => {
        this.studentData = data;
        this.courses = data.courses || [];
        this.diplomas = data.diplomas || [];
        this.otherTrainings = data.otherTrainings || [];
        
        this.studentForm.patchValue({
          id: data.id,
          ine: data.ine,
          firstName: data.firstName,
          lastName: data.lastName,
          dateOfBirth: new Date(data.dateOfBirth),
          formation: data.formation,
          promotion: data.promotion,
          startYear: data.startYear,
          endYear: data.endYear,
          email: data.email,
          phoneNumber: data.phoneNumber
        });
        
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading student profile', error);
        this.loading = false;
      }
    });
  }

  onSubmit() {
    if (this.studentForm.invalid) {
      return;
    }

    const studentData = this.studentForm.value;
    
    this.apiService.put(`/api/students/${studentData.id}`, studentData).subscribe({
      next: (data: any) => {
        this.studentData = data;
        this.formMode = 'view';
      },
      error: (error) => {
        console.error('Error updating student profile', error);
      }
    });
  }

  switchToEditMode() {
    this.formMode = 'edit';
  }

  cancelEdit() {
    // Reset form to original values
    this.studentForm.patchValue({
      id: this.studentData.id,
      ine: this.studentData.ine,
      firstName: this.studentData.firstName,
      lastName: this.studentData.lastName,
      dateOfBirth: new Date(this.studentData.dateOfBirth),
      formation: this.studentData.formation,
      promotion: this.studentData.promotion,
      startYear: this.studentData.startYear,
      endYear: this.studentData.endYear,
      email: this.studentData.email,
      phoneNumber: this.studentData.phoneNumber
    });
    
    this.formMode = 'view';
  }

  searchStudents() {
    if (this.searchForm.invalid) {
      return;
    }

    this.searching = true;
    const query = this.searchForm.get('query')?.value;
    
    this.apiService.get('/api/students/search', { query }).subscribe({
      next: (data: any) => {
        this.searchResults = data;
        this.searching = false;
      },
      error: (error) => {
        console.error('Error searching students', error);
        this.searching = false;
      }
    });
  }

  selectStudent(id: number) {
    this.loadStudentProfile(id);
    this.searchResults = [];
    this.searchForm.reset();
  }
}
