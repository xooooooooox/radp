# RADP - Rapid Application Development Platform

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

[![GitHub License](https://img.shields.io/github/license/xooooooooox/radp?style=for-the-badge)](LICENSE)
[![GitHub Release](https://img.shields.io/github/v/release/xooooooooox/radp?style=for-the-badge)](https://github.com/xooooooooox/radp/releases)
[![Maven Central Version](https://img.shields.io/maven-central/v/space.x9x.radp/radp?style=for-the-badge)](https://central.sonatype.com/namespace/space.x9x.radp)

[[English]](./README.md)  [[中文文档]](./README_CN.md) [[Documentation]](https://xooooooooox.github.io/radp)

## Introduction

RADP is a one-stop solution designed to streamline enterprise-level development. By standardizing dependencies,
integrating common components, and providing out-of-the-box tooling, RADP significantly reduces development complexity
and maintenance overhead.

## Features

- **Unified Dependency Management & Plugin Wrappers**: RADP manages library versions centrally to prevent dependency conflicts and encapsulates frequently used Maven plugins.
This ensures consistent builds and frees developers from the tedium of managing complex build configurations.
- **Common Components Integration**: Built upon the Spring ecosystem, RADP integrates and extends several essential components:
  - XxlJob for distributed scheduling
  - CAT for application performance monitoring
  - Netty for high-performance network communication
  - Arthas for diagnostics and troubleshooting
- **Extensible Adaptation Layers**: RADP offers abstract layers and integration points for mainstream enterprise technologies:
  - Message Queues (e.g., Kafka, RabbitMQ)
  - Caching frameworks and multi-level caching strategies
  - SMS Platforms
  - Email Integrations
  - Excel Handling for importing, exporting, and processing data
- **Scaffolding & Architecture Templates**: RADP provides scaffolding tools and recommended architectural patterns to jumpstart development:
  - DDD (Domain-Driven Design) and MVC application templates
  - Unified coding standards and pipeline orchestration
  - Simplified DevOps workflows and CI/CD integration

## Contributing

> Thanks to following people who contributed to this project. 🎉🎉🙏🙏

<a href="https://github.com/xooooooooox/radp/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=xooooooooox/radp" />
</a>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=xooooooooox/radp&type=Date)

## COPYRIGHT

[GNU General Public License](./LICENSE) - Copyright (C) 2024 xooooooooox
and [contributors](https://github.com/xooooooooox/radp/graphs/contributors)