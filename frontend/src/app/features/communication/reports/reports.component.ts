import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatColumnDef, MatHeaderCellDef, MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ApiService } from '../../../core/services/api.service';
import { AuthService } from '../../../core/auth/auth.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
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
       MatNativeDateModule,
       MatTableModule,
       MatPaginatorModule,
       MatPaginatorModule,
       MatSortModule,
       MatDialogModule,
       MatProgressSpinnerModule,
     MatColumnDef,
     MatHeaderCellDef,
    ],
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {
  reportForm: FormGroup;
  reports: any[] = [];
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['title', 'type', 'date', 'createdBy', 'actions'];
  reportTypes = [
    { id: 'MEETING', label: 'Réunion' },
    { id: 'ENCOUNTER', label: 'Rencontre' },
    { id: 'SEMINAR', label: 'Séminaire' },
    { id: 'WEBINAR', label: 'Webinaire' },
    { id: 'COUNCIL', label: 'Conseil d\'Université' }
  ];
  loading = true;
  formMode = 'create';
  selectedReportId: number | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService,
    private dialog: MatDialog
  ) {
    this.reportForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      type: ['', Validators.required],
      date: [new Date(), Validators.required],
      content: ['', [Validators.required, Validators.maxLength(5000)]],
      participants: ['', Validators.maxLength(500)]
    });
    
    this.dataSource = new MatTableDataSource<any>([]);
  }

  ngOnInit(): void {
    this.loadReports();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadReports() {
    this.loading = true;
    this.apiService.get('/api/reports').subscribe({
      next: (data: any) => {
        this.reports = data;
        this.dataSource.data = this.reports;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading reports', error);
        this.loading = false;
      }
    });
  }

  onSubmit() {
    if (this.reportForm.invalid) {
      return;
    }

    const report = this.reportForm.value;
    
    if (this.formMode === 'create') {
      this.apiService.post('/api/reports', report).subscribe({
        next: () => {
          this.resetForm();
          this.loadReports();
        },
        error: (error) => {
          console.error('Error creating report', error);
        }
      });
    } else {
      this.apiService.put(`/api/reports/${this.selectedReportId}`, report).subscribe({
        next: () => {
          this.resetForm();
          this.loadReports();
        },
        error: (error) => {
          console.error('Error updating report', error);
        }
      });
    }
  }

  editReport(report: any) {
    this.formMode = 'edit';
    this.selectedReportId = report.id;
    this.reportForm.patchValue({
      title: report.title,
      type: report.type,
      date: new Date(report.date),
      content: report.content,
      participants: report.participants
    });
  }

  deleteReport(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce rapport ?')) {
      this.apiService.delete(`/api/reports/${id}`).subscribe({
        next: () => {
          this.loadReports();
        },
        error: (error) => {
          console.error('Error deleting report', error);
        }
      });
    }
  }

  resetForm() {
    this.formMode = 'create';
    this.selectedReportId = null;
    this.reportForm.reset({
      date: new Date()
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  canEdit(report: any): boolean {
    const currentUser = this.authService.currentUserValue;
    if (!currentUser) return false;
    
    // Admins can edit all reports
    if (currentUser.isAdmin) return true;
    
    // Users can edit their own reports
    return report.createdBy === currentUser.id;
  }
}
