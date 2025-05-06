import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { ApiService } from '../../../core/services/api.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-document-management',
    standalone: true,
    imports: [
         CommonModule,
         ReactiveFormsModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          MatSelectModule,
          MatTableModule,
          MatPaginatorModule,
          MatTabsModule,
          MatProgressSpinnerModule
    ],
  templateUrl: './document-management.component.html',
  styleUrls: ['./document-management.component.scss']
})
export class DocumentManagementComponent implements OnInit {
  documentForm: FormGroup;
  documents: any[] = [];
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['title', 'type', 'creationDate', 'creator', 'actions'];
  documentTypes = [
    { id: 'MAIL_IN', label: 'Courrier Arrivé' },
    { id: 'MAIL_OUT', label: 'Courrier Départ' },
    { id: 'INTERNAL_NOTE', label: 'Note de Service Interne' },
    { id: 'EXTERNAL_NOTE', label: 'Note de Service Externe' },
    { id: 'ADMIN_NOTE', label: 'Note Administrative' },
    { id: 'CIRCULAR', label: 'Circulaire' },
    { id: 'BUDGET_PLAN', label: 'Projet de Budget' },
    { id: 'BUDGET_REPORT', label: 'Budget Réalisé' },
    { id: 'PERSONNEL_FILE', label: 'Dossier de Personnel' },
    { id: 'STUDENT_FILE', label: 'Dossier d\'Étudiant' }
  ];
  
  loading = true;
  formMode = 'create';
  selectedDocumentId: number | null = null;
  activeTab = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService
  ) {
    this.documentForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      type: ['', Validators.required],
      reference: ['', Validators.maxLength(50)],
      description: ['', Validators.maxLength(500)],
      content: ['', Validators.required],
      visibleTo: ['', Validators.required]
    });
    
    this.dataSource = new MatTableDataSource<any>([]);
  }

  ngOnInit(): void {
    this.loadDocuments();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadDocuments() {
    this.loading = true;
    this.apiService.get('/api/documents').subscribe({
      next: (data: any) => {
        this.documents = data;
        this.dataSource.data = this.documents;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading documents', error);
        this.loading = false;
      }
    });
  }

  onSubmit() {
    if (this.documentForm.invalid) {
      return;
    }

    const document = this.documentForm.value;
    
    if (this.formMode === 'create') {
      this.apiService.post('/api/documents', document).subscribe({
        next: () => {
          this.resetForm();
          this.loadDocuments();
          this.activeTab = 0; // Switch to list tab
        },
        error: (error) => {
          console.error('Error creating document', error);
        }
      });
    } else {
      this.apiService.put(`/api/documents/${this.selectedDocumentId}`, document).subscribe({
        next: () => {
          this.resetForm();
          this.loadDocuments();
          this.activeTab = 0; // Switch to list tab
        },
        error: (error) => {
          console.error('Error updating document', error);
        }
      });
    }
  }

  editDocument(document: any) {
    this.formMode = 'edit';
    this.selectedDocumentId = document.id;
    this.documentForm.patchValue({
      title: document.title,
      type: document.type,
      reference: document.reference,
      description: document.description,
      content: document.content,
      visibleTo: document.visibleTo
    });
    this.activeTab = 1; // Switch to form tab
  }

  deleteDocument(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce document ?')) {
      this.apiService.delete(`/api/documents/${id}`).subscribe({
        next: () => {
          this.loadDocuments();
        },
        error: (error) => {
          console.error('Error deleting document', error);
        }
      });
    }
  }

  resetForm() {
    this.formMode = 'create';
    this.selectedDocumentId = null;
    this.documentForm.reset();
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
      this.loadDocuments();
    }
  }

  getDocumentTypeName(typeId: string): string {
    const type = this.documentTypes.find(t => t.id === typeId);
    return type ? type.label : typeId;
  }
}
