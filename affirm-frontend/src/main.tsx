import React from 'react'
import ReactDOM from 'react-dom/client'
import { ConfigProvider, App as AntApp, Button, Typography } from 'antd'

function App() {
  return (
    <AntApp>
      <div style={{ maxWidth: 680, margin: '64px auto', textAlign: 'center' }}>
        <Typography.Title level={2}>Affirmation Counter</Typography.Title>
        <Typography.Paragraph>前端初始化成功。请先启动后端并完成 /ping 自检。</Typography.Paragraph>
        <Button type="primary" size="large">点我 +1</Button>
      </div>
    </AntApp>
  )
}

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ConfigProvider>
      <App />
    </ConfigProvider>
  </React.StrictMode>
)


