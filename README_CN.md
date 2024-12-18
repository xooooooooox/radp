# RADP

```text
 ███████████     █████████   ██████████   ███████████ 
░░███░░░░░███   ███░░░░░███ ░░███░░░░███ ░░███░░░░░███
 ░███    ░███  ░███    ░███  ░███   ░░███ ░███    ░███
 ░██████████   ░███████████  ░███    ░███ ░██████████ 
 ░███░░░░░███  ░███░░░░░███  ░███    ░███ ░███░░░░░░  
 ░███    ░███  ░███    ░███  ░███    ███  ░███        
 █████   █████ █████   █████ ██████████   █████       
░░░░░   ░░░░░ ░░░░░   ░░░░░ ░░░░░░░░░░   ░░░░░        
```

[[English]](./README.md)  [[中文文档]](./README_CN.md) [[完整文档]](https://xooooooooox.github.io/radp)

## 介绍

RADP 是一个面向企业级开发的一站式解决方案。通过对依赖的标准化管理、通用组件的整合以及开箱即用的工具支持，RADP
显著降低了开发的复杂性和后期维护成本。

## 特性

- **依赖管理和插件封装**：统一管理依赖版本，解决依赖冲突问题，并提供常用 Maven 插件的封装，让开发者减少在构建工具所消耗的时间。
- **常用组件集成与封装**：基于 Spring 生态，RADP 集成并扩展了多种企业级必备组件：
  - **XxlJob**：分布式任务调度
  - **CAT**：应用性能监控
  - **Netty**：高性能网络通信
  - **Arthas**：诊断与故障排查
  - ... ...
- **组件适配及扩展点**：针对现有主流技术点进行高级抽象，提供 `消息队列`，`缓存`，`短信平台`，`邮件`，`Excel` 等组件的集成。
- **脚手架封装**：RADP 提供完善的脚手架工具和推荐的架构模式，帮助团队快速启动项目.
  - DDD（领域驱动设计）和 MVC 应用模板
  - 统一的编码规范和流水线编排
  - 简化工程 DevOPS 化过程
  - ... ...
- **通用场景解决方案**：提供 `多级缓存`，`分布式锁`，`分布式唯一 ID`，`幂等性处理`，`业务流程编排`，`最终一致性`，`全链路标记`，
  `脱敏` 等解决方案工具。

## 贡献者

> 感谢以下贡献者对本项目的贡献。🎉🎉🙏🙏

<a href="https://github.com/xooooooooox/radp/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=xooooooooox/radp" />
</a>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=xooooooooox/radp&type=Date)

## COPYRIGHT

[GNU General Public License](./LICENSE) - Copyright (C) 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)