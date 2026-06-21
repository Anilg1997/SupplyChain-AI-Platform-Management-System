import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

interface RagResult {
  content: string;
  source: string;
  similarity: number;
  documentId: string;
}

@Component({
  selector: 'app-ai-rag',
  standalone: false,
  templateUrl: './ai-rag.component.html',
  styleUrl: './ai-rag.component.scss'
})
export class AiRagComponent {
  searchForm: FormGroup;
  searchResults: RagResult[] = [];
  loading = false;
  searchHistory: string[] = [];
  suggestedQueries: string[] = [
    'Show me top suppliers by quality score',
    'What inventory items need reordering?',
    'Summarize contract #CT-2024-0012'
  ];
  chatMessage = '';

  private readonly mockResults: RagResult[] = [
    {
      content: 'Supplier evaluation criteria include quality score (35%), on-time delivery (25%), compliance rating (20%), cost competitiveness (15%), and responsiveness (5%)',
      source: 'supplier-evaluation-guide.pdf',
      similarity: 0.92,
      documentId: 'DOC-001'
    },
    {
      content: 'Inventory reorder thresholds are calculated using lead time demand plus safety stock. Safety stock is determined by service level and demand variability.',
      source: 'inventory-management-policy.docx',
      similarity: 0.87,
      documentId: 'DOC-002'
    },
    {
      content: 'Contract CT-2024-0012 with Acme Corp covers raw material supply for Q1-Q4 2025. Total value $2.4M with net-60 payment terms and annual renewal clause.',
      source: 'contracts-database',
      similarity: 0.78,
      documentId: 'DOC-003'
    },
    {
      content: 'Quality inspection results for Batch #B-2024-089: Pass rate 98.2%, defect rate 1.8%. Non-conformances found in dimensional tolerance and surface finish.',
      source: 'quality-report-nov2024.xlsx',
      similarity: 0.71,
      documentId: 'DOC-004'
    }
  ];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.searchForm = this.fb.group({
      query: ['', Validators.required],
      maxResults: [5]
    });
  }

  onSearch(): void {
    if (this.searchForm.invalid) return;

    const query = this.searchForm.get('query')?.value || '';
    this.loading = true;
    this.searchResults = [];

    setTimeout(() => {
      this.searchResults = [...this.mockResults];
      this.loading = false;
      if (query && !this.searchHistory.includes(query)) {
        this.searchHistory.unshift(query);
      }
      this.snackBar.open(`Found ${this.searchResults.length} results`, 'Close', { duration: 3000 });
    }, 1500);
  }

  askQuestion(): void {
    if (!this.chatMessage?.trim()) return;
    this.snackBar.open(`AI: "${this.chatMessage}" — This is a mock response for demo purposes.', 'Close', { duration: 4000 });
    this.chatMessage = '';
  }

  clearHistory(): void {
    this.searchHistory = [];
  }

  useSuggestedQuery(query: string): void {
    this.searchForm.patchValue({ query });
    this.onSearch();
  }
}
