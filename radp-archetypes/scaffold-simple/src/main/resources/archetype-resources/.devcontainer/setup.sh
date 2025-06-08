#!/bin/bash

# Define color codes
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check if yq is installed
check_yq() {
  if ! command -v yq &>/dev/null; then
    echo -e "${YELLOW}yq is not installed. It is required to process YAML files.${NC}"
    read -p "Do you want to install yq? (y/n): " choice
    case "$choice" in
    y | Y) install_yq ;;
    *)
      echo -e "${RED}yq is required for this script to work. Exiting.${NC}"
      exit 1
      ;;
    esac
  fi
}

# Function to check if jq is installed
check_jq() {
  if ! command -v jq &>/dev/null; then
    echo -e "${YELLOW}jq is not installed. It is required to process JSON files.${NC}"
    read -p "Do you want to install jq? (y/n): " choice
    case "$choice" in
    y | Y) install_jq ;;
    *)
      echo -e "${RED}jq is required for this script to work. Exiting.${NC}"
      exit 1
      ;;
    esac
  fi
}

# Function to install yq
install_yq() {
  echo -e "${BLUE}Installing yq...${NC}"
  if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux installation
    sudo wget -qO /usr/local/bin/yq https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64
    sudo chmod a+x /usr/local/bin/yq
  elif [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS installation
    if command -v brew &>/dev/null; then
      brew install yq
    else
      echo -e "${RED}Homebrew is not installed. Please install Homebrew first or install yq manually.${NC}"
      exit 1
    fi
  else
    echo -e "${RED}Unsupported operating system. Please install yq manually.${NC}"
    exit 1
  fi

  # Verify installation
  if ! command -v yq &>/dev/null; then
    echo -e "${RED}Failed to install yq. Please install it manually.${NC}"
    exit 1
  else
    echo -e "${GREEN}yq installed successfully.${NC}"
  fi
}

# Function to install jq
install_jq() {
  echo -e "${BLUE}Installing jq...${NC}"
  if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux installation
    sudo apt-get update && sudo apt-get install -y jq
  elif [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS installation
    if command -v brew &>/dev/null; then
      brew install jq
    else
      echo -e "${RED}Homebrew is not installed. Please install Homebrew first or install jq manually.${NC}"
      exit 1
    fi
  else
    echo -e "${RED}Unsupported operating system. Please install jq manually.${NC}"
    exit 1
  fi

  # Verify installation
  if ! command -v jq &>/dev/null; then
    echo -e "${RED}Failed to install jq. Please install it manually.${NC}"
    exit 1
  else
    echo -e "${GREEN}jq installed successfully.${NC}"
  fi
}

# Function to read configuration from setup.yml
read_config() {
  echo -e "${BLUE}Reading configuration from setup.yml...${NC}"

  # Check if setup.yml exists
  if [ ! -f "setup.yml" ]; then
    echo -e "${RED}setup.yml not found in the current directory.${NC}"
    exit 1
  fi
}

# Function to update devcontainer.json
update_devcontainer_json() {
  echo -e "${BLUE}Updating devcontainer.json...${NC}"

  # Create a temporary file for the updated devcontainer.json
  temp_file=$(mktemp)

  # Get enabled templates
  enabled_templates=($(yq e '.devcontainer.templates.enabled[]' setup.yml))

  # Create the postCreateCommand with enabled templates
  post_create_command="cd .devcontainer && docker compose --env-file .env"
  for template in "${enabled_templates[@]}"; do
    post_create_command+=" -f docker-compose.${template}.yml"
  done
  post_create_command+=" up -d"

  # Read the devcontainer.json file
  if [ -f "devcontainer.json" ]; then
    # First, create a clean version of devcontainer.json without any existing postCreateCommand
    grep -v '"postCreateCommand":' devcontainer.json >"$temp_file"

    # Find the line with the last property to insert postCreateCommand before the closing brace
    line_number=$(grep -n "}" "$temp_file" | tail -1 | cut -d: -f1)

    # Create the final file with the new postCreateCommand
    head -n $((line_number - 1)) "$temp_file" >"$temp_file.2"
    echo "  \"postCreateCommand\": \"$post_create_command\"" >>"$temp_file.2"
    echo "}" >>"$temp_file.2"

    mv "$temp_file.2" devcontainer.json
    echo -e "${GREEN}devcontainer.json updated successfully.${NC}"
  else
    echo -e "${RED}devcontainer.json not found in the current directory.${NC}"
    exit 1
  fi
}

# Function to update docker-compose.yml
update_docker_compose_yml() {
  echo -e "${BLUE}Updating docker-compose.yml...${NC}"

  # Get configuration values
  java_version=$(yq e '.devcontainer.languages.java.version' setup.yml)
  nodejs_version=$(yq e '.devcontainer.languages.nodejs.version' setup.yml)
  maven_enabled=$(yq e '.devcontainer.build.tools.maven.enabled' setup.yml)
  maven_version=$(yq e '.devcontainer.build.tools.maven.version' setup.yml)
  gradle_enabled=$(yq e '.devcontainer.build.tools.gradle.enabled' setup.yml)
  gradle_version=$(yq e '.devcontainer.build.tools.gradle.version' setup.yml)

  # Create a temporary file for the updated docker-compose.yml
  temp_file=$(mktemp)

  # First, preserve the original structure by copying the file
  cp docker-compose.yml "$temp_file"

  # Update docker-compose.yml using yq with inplace option to preserve formatting
  yq e -i ".services.devcontainer.build.args.VARIANT = \"$java_version\"" "$temp_file"
  yq e -i ".services.devcontainer.build.args.INSTALL_MAVEN = \"$maven_enabled\"" "$temp_file"
  yq e -i ".services.devcontainer.build.args.MAVEN_VERSION = \"$maven_version\"" "$temp_file"
  yq e -i ".services.devcontainer.build.args.INSTALL_GRADLE = \"$gradle_enabled\"" "$temp_file"
  yq e -i ".services.devcontainer.build.args.NODE_VERSION = \"$nodejs_version\"" "$temp_file"

  # Move the temporary file to docker-compose.yml
  mv "$temp_file" docker-compose.yml

  echo -e "${GREEN}docker-compose.yml updated successfully.${NC}"
}

# Function to copy template docker-compose files
copy_template_files() {
  echo -e "${BLUE}Copying template docker-compose files...${NC}"

  # Get enabled templates
  enabled_templates=($(yq e '.devcontainer.templates.enabled[]' setup.yml))

  # Copy template files
  for template in "${enabled_templates[@]}"; do
    template_file="templates/$template/docker-compose.yml"
    target_file="docker-compose.$template.yml"

    if [ -f "$template_file" ]; then
      cp -v "$template_file" "$target_file"
    else
      echo -e "${YELLOW}Warning: Template file $template_file not found.${NC}"
    fi
  done

  echo -e "${GREEN}Template files copied successfully.${NC}"
}

# Main function
main() {
  # Check if yq is installed
  check_yq

  # Check if jq is installed
  check_jq

  # Read configuration
  read_config

  # Update devcontainer.json
  update_devcontainer_json

  # Update docker-compose.yml
  update_docker_compose_yml

  # Copy template docker-compose files
  copy_template_files

  echo -e "${GREEN}Setup completed successfully.${NC}"
}

# Run the main function
main
