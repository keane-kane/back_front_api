import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { ApiService } from '../../../core/services/api.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-student-tracking',
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
          MatTableModule,
          MatPaginatorModule,
          MatSortModule,
          MatTabsModule,
          MatProgressSpinnerModule,
          MatDatepickerModule,
          MatChipsModule
     ],
  templateUrl: './student-tracking.component.html',
  styleUrls: ['./student-tracking.component.scss']
})
export class StudentTrackingComponent implements OnInit {
  trackingForm: FormGroup;
  students: any[] = [];
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['name', 'formation', 'graduationYear', 'employmentStatus', 'actions'];
  employmentStatuses = [
    { id: 'EMPLOYED', label: 'Emploi Salarié' },
    { id: 'SELF_EMPLOYED', label: 'Auto-Emploi' },
    { id: 'SEEKING', label: 'En Recherche' },
    { id: 'FURTHER_STUDIES', label: 'Poursuite d\'Études' },
    { id: 'INTERNSHIP', label: 'Stage' },
    { id: 'UNKNOWN', label: 'Inconnu' }
  ];
  
  loading = true;
  selectedStudentId: number | null = null;
  activeTab = 0;
  statistics: any = {};

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService
  ) {
    this.trackingForm = this.formBuilder.group({
      studentId: ['', Validators.required],
      employmentStatus: ['', Validators.required],
      company: [''],
      position: [''],
      startDate: [''],
      comments: [''],
      contactInfo: ['']
    });
    
    this.dataSource = new MatTableDataSource<any>([]);
  }

  ngOnInit(): void {
    this.loadStudents();
    this.loadStatistics();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadStudents() {
    this.loading = true;
    this.apiService.get('/api/insertion/graduates').subscribe({
      next: (data: any) => {
        this.students = data;
        this.dataSource.data = this.students;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading graduates', error);
        this.loading = false;
      }
    });
  }

  loadStatistics() {
    this.apiService.get('/api/insertion/statistics').subscribe({
      next: (data: any) => {
        this.statistics = data;
      },
      error: (error) => {
        console.error('Error loading statistics', error);
      }
    });
  }

  onSubmit() {
    if (this.trackingForm.invalid) {
      return;
    }

    const trackingData = this.trackingForm.value;
    
    this.apiService.post('/api/insertion/tracking', trackingData).subscribe({
      next: () => {
        this.resetForm();
        this.loadStudents();
        this.loadStatistics();
        this.activeTab = 0; // Switch to list tab
      },
      error: (error) => {
        console.error('Error updating tracking data', error);
      }
    });
  }

  editTracking(student: any) {
    this.selectedStudentId = student.id;
    this.trackingForm.patchValue({
      studentId: student.id,
      employmentStatus: student.employmentStatus || 'UNKNOWN',
      company: student.company || '',
      position: student.position || '',
      startDate: student.startDate ? new Date(student.startDate) : null,
      comments: student.comments || '',
      contactInfo: student.contactInfo || ''
    });
    this.activeTab = 1; // Switch to form tab
  }

  deleteTracking(id: number) {
    if (confirm('Êtes-vous sûr de vouloir réinitialiser le suivi de cet étudiant ?')) {
      this.apiService.delete(`/api/insertion/tracking/${id}`).subscribe({
        next: () => {
          this.loadStudents();
          this.loadStatistics();
        },
        error: (error) => {
          console.error('Error deleting tracking data', error);
        }
      });
    }
  }

  resetForm() {
    this.selectedStudentId = null;
    this.trackingForm.reset();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  onTabChange(event: any) {
    this.activeTab = event.index;
    if (event.index === 0) {
      this.loadStudents();
      this.loadStatistics();
    }
  }

  getEmploymentStatusLabel(statusId: string): string {
    const status = this.employmentStatuses.find(s => s.id === statusId);
    return status ? status.label : 'Inconnu';
  }

  getEmploymentStatusClass(statusId: string): string {
    switch(statusId) {
      case 'EMPLOYED':
        return 'status-employed';
      case 'SELF_EMPLOYED':
        return 'status-self-employed';
      case 'SEEKING':
        return 'status-seeking';
      case 'FURTHER_STUDIES':
        return 'status-studies';
      case 'INTERNSHIP':
        return 'status-internship';
      default:
        return 'status-unknown';
    }
  }
}
