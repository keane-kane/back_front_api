import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Generic GET method
  get<T>(path: string, params: any = {}): Observable<T> {
    const options = { params: new HttpParams({ fromObject: params }) };
    return this.http.get<T>(`${this.baseUrl}${path}`, options);
  }

  // Generic POST method
  post<T>(path: string, body: any = {}): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${path}`, body);
  }

  // Generic PUT method
  put<T>(path: string, body: any = {}): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}${path}`, body);
  }

  // Generic DELETE method
  delete<T>(path: string): Observable<T> {
    return this.http.delete<T>(`${this.baseUrl}${path}`);
  }

  // Generic method to download files
  download(path: string, params: any = {}): Observable<Blob> {
    const options = { 
      params: new HttpParams({ fromObject: params }),
      responseType: 'blob' as 'json'
    };
    return this.http.get(`${this.baseUrl}${path}`, options) as Observable<Blob>;
  }
}
