export interface Metadata {
    name: string;
    labels?: {
      [key: string]: string;
    } | null;
    annotations?: {
      [key: string]: string;
    } | null;
    version?: number | null;
    creationTimestamp?: string | null;
    deletionTimestamp?: string | null;
  }
  
  export interface NotifyMeSpec {
    apiToken: string;
    channel: string;
    status?: boolean;
    commentStatus?: boolean;
    postStatus?: boolean;
    likeStatus?: boolean;
  }
  
  /**
   * 与自定义模型对应
   */
  export interface NotifyMe {
    spec: NotifyMeSpec;
    apiVersion: "notifyme.plugin.halo.run/v1alpha1"; // apiVersion=自定义模型的 group/version
    kind: "NotifyMe"; //自定义模型中 @GVK 注解中的 kind
    metadata: Metadata;
  }
  
  /**
   * Todo 自定义模型生成 list API 所对应的类型
   */
  export interface NotifyMeList {
    page: number;
    size: number;
    total: number;
    items: Array<NotifyMe>;
    first: boolean;
    last: boolean;
    hasNext: boolean;
    hasPrevious: boolean;
    totalPages: number;
  }