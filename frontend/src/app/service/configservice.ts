import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ExcelBody {
  filename: string;
  rows: { [key: string]: string }[];
}

@Injectable({ providedIn: 'root' })
export class CofigService {
  private http = inject(HttpClient);
  private api = 'http://localhost:8080';

  list(): Observable<string[]> {
    return this.http.get<string[]>(`${this.api}/list`);
  }

  read(filename: string): Observable<ExcelBody> {
    const params = new HttpParams().set('filename', filename);
    return this.http.get<ExcelBody>(`${this.api}/read`, { params });
  }

  write(fileData: ExcelBody): Observable<ExcelBody> {
    return this.http.post<ExcelBody>(`${this.api}/write`, fileData);
  }

  download(filename: string) {
    const params = new HttpParams().set('filename', filename);
    return this.http.get(`${this.api}/download/${encodeURIComponent(filename)}`, {
      responseType: 'blob',
    });
  }

  upload(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.api}/upload`, formData);
  }
}
