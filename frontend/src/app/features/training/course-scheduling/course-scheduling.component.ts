import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-course-scheduling',
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
          MatExpansionModule,
          MatDialogModule
    ],
  templateUrl: './course-scheduling.component.html',
  styleUrls: ['./course-scheduling.component.scss']
})
export class CourseSchedulingComponent implements OnInit {
  courseForm: FormGroup;
  courses: any[] = [];
  instructors: any[] = [];
  formations: any[] = [];
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['title', 'formation', 'instructor', 'startDate', 'endDate', 'location', 'actions'];
  
  loading = true;
  formMode = 'create';
  selectedCourseId: number | null = null;
  activeTab = 0;
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    public authService: AuthService
  ) {
    this.courseForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      formationId: ['', Validators.required],
      instructorId: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      location: ['', Validators.required],
      description: [''],
      maxStudents: [30, [Validators.required, Validators.min(1)]]
    });
    
    this.dataSource = new MatTableDataSource<any>([]);
  }

  ngOnInit(): void {
    this.loadCourses();
    this.loadInstructors();
    this.loadFormations();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadCourses() {
    this.loading = true;
    this.apiService.get('/api/training/courses').subscribe({
      next: (data: any) => {
        this.courses = data;
        this.dataSource.data = this.courses;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading courses', error);
        this.loading = false;
      }
    });
  }

  loadInstructors() {
    this.apiService.get('/api/training/instructors').subscribe({
      next: (data: any) => {
        this.instructors = data;
      },
      error: (error) => {
        console.error('Error loading instructors', error);
      }
    });
  }

  loadFormations() {
    this.apiService.get('/api/training/formations').subscribe({
      next: (data: any) => {
        this.formations = data;
      },
      error: (error) => {
        console.error('Error loading formations', error);
      }
    });
  }

  onSubmit() {
    if (this.courseForm.invalid) {
      return;
    }

    const course = this.courseForm.value;
    
    if (this.formMode === 'create') {
      this.apiService.post('/api/training/courses', course).subscribe({
        next: () => {
          this.resetForm();
          this.loadCourses();
          this.activeTab = 0; // Switch to list tab
        },
        error: (error) => {
          console.error('Error creating course', error);
        }
      });
    } else {
      this.apiService.put(`/api/training/courses/${this.selectedCourseId}`, course).subscribe({
        next: () => {
          this.resetForm();
          this.loadCourses();
          this.activeTab = 0; // Switch to list tab
        },
        error: (error) => {
          console.error('Error updating course', error);
        }
      });
    }
  }

  editCourse(course: any) {
    this.formMode = 'edit';
    this.selectedCourseId = course.id;
    this.courseForm.patchValue({
      title: course.title,
      formationId: course.formationId,
      instructorId: course.instructorId,
      startDate: new Date(course.startDate),
      endDate: new Date(course.endDate),
      location: course.location,
      description: course.description,
      maxStudents: course.maxStudents
    });
    this.activeTab = 1; // Switch to form tab
  }

  deleteCourse(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce cours ?')) {
      this.apiService.delete(`/api/training/courses/${id}`).subscribe({
        next: () => {
          this.loadCourses();
        },
        error: (error) => {
          console.error('Error deleting course', error);
        }
      });
    }
  }

  resetForm() {
    this.formMode = 'create';
    this.selectedCourseId = null;
    this.courseForm.reset({
      maxStudents: 30
    });
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
      this.loadCourses();
    }
  }

  canEdit(): boolean {
    const user = this.authService.currentUserValue;
    if (!user) return false;
    return user.isAdmin || user.isAdministrative;
  }

  exportSchedule() {
    this.apiService.download('/api/training/courses/export', {}).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'emploi_du_temps.pdf';
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.error('Error exporting schedule', error);
      }
    });
  }
}
