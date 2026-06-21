import { Component } from '@angular/core';

interface SystemStat {
  label: string;
  value: string;
  icon: string;
  color: string;
}

interface AdminUser {
  username: string;
  email: string;
  role: string;
  enabled: boolean;
  lastLogin: string;
}

interface SystemConfig {
  key: string;
  value: string;
  description: string;
}

interface RecentActivity {
  action: string;
  user: string;
  timestamp: string;
  status: string;
}

interface AiModel {
  name: string;
  version: string;
  type: string;
  status: string;
  memory: string;
}

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent {
  systemStats: SystemStat[] = [
    { label: 'Total Users', value: '24', icon: 'people', color: '#1976d2' },
    { label: 'Active Services', value: '34', icon: 'settings', color: '#388e3c' },
    { label: 'AI Models', value: '2', icon: 'psychology', color: '#7b1fa2' },
    { label: 'System Uptime', value: '14d 6h', icon: 'timer', color: '#e65100' }
  ];

  adminUsers: AdminUser[] = [
    { username: 'admin', email: 'admin@supplychainpro.com', role: 'SUPER_ADMIN', enabled: true, lastLogin: '2024-12-10 08:30:00' },
    { username: 'jdoe', email: 'jdoe@supplychainpro.com', role: 'ADMIN', enabled: true, lastLogin: '2024-12-09 16:45:00' },
    { username: 'asmith', email: 'asmith@supplychainpro.com', role: 'OPERATOR', enabled: true, lastLogin: '2024-12-08 11:20:00' },
    { username: 'bwilson', email: 'bwilson@supplychainpro.com', role: 'VIEWER', enabled: false, lastLogin: '2024-11-30 09:00:00' },
    { username: 'klee', email: 'klee@supplychainpro.com', role: 'OPERATOR', enabled: true, lastLogin: '2024-12-10 07:15:00' }
  ];

  systemConfigs: SystemConfig[] = [
    { key: 'ai.rag.enabled', value: 'true', description: 'Enable AI RAG search functionality' },
    { key: 'ai.llm.model', value: 'llama3.1', description: 'Default LLM model for AI operations' },
    { key: 'ai.rag.max.chunk.size', value: '1024', description: 'Maximum document chunk size for indexing' },
    { key: 'ai.rag.top.k', value: '5', description: 'Number of top results to retrieve' },
    { key: 'ai.embedding.model', value: 'nomic-embed-text', description: 'Embedding model for vector search' },
    { key: 'security.session.timeout', value: '3600', description: 'Session timeout in seconds' },
    { key: 'system.log.level', value: 'INFO', description: 'Application log level' },
    { key: 'notification.email.enabled', value: 'true', description: 'Enable email notifications' }
  ];

  recentActivities: RecentActivity[] = [
    { action: 'User login', user: 'jdoe', timestamp: '2024-12-10 08:30:00', status: 'success' },
    { action: 'Configuration update', user: 'admin', timestamp: '2024-12-10 08:15:00', status: 'success' },
    { action: 'Model deployment', user: 'admin', timestamp: '2024-12-09 22:00:00', status: 'success' },
    { action: 'User disabled', user: 'admin', timestamp: '2024-12-09 14:20:00', status: 'warning' },
    { action: 'System backup', user: 'system', timestamp: '2024-12-09 03:00:00', status: 'success' },
    { action: 'Failed login attempt', user: 'unknown', timestamp: '2024-12-08 23:45:00', status: 'error' },
    { action: 'AI model retrained', user: 'admin', timestamp: '2024-12-08 12:00:00', status: 'success' },
    { action: 'Index rebuild completed', user: 'system', timestamp: '2024-12-07 06:30:00', status: 'success' }
  ];

  aiModels: AiModel[] = [
    { name: 'Llama 3.1', version: '3.1-8b', type: 'LLM', status: 'Running', memory: '8.2 GB' },
    { name: 'Nomic Embed Text', version: 'v1.5', type: 'Embedding', status: 'Running', memory: '1.8 GB' }
  ];

  displayedColumnsUsers = ['username', 'email', 'role', 'enabled', 'actions'];
  displayedColumnsConfigs = ['key', 'value', 'description', 'actions'];

  toggleUserStatus(user: AdminUser): void {
    user.enabled = !user.enabled;
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      success: 'status-success',
      warning: 'status-warning',
      error: 'status-error'
    };
    return map[status] || 'status-default';
  }
}
